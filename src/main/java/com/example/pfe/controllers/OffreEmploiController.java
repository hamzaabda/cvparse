package com.example.pfe.controllers;

import com.example.pfe.models.OffreEmploi;
import com.example.pfe.services.NotificationService;
import com.example.pfe.services.OffreEmploiService;
import com.example.pfe.email.EmailService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
public class OffreEmploiController {

    @Autowired
    private OffreEmploiService offreEmploiService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    // Endpoint to create a new OffreEmploi
    @PostMapping
    public ResponseEntity<OffreEmploi> createOffreEmploi(@RequestBody OffreEmploi offreEmploi) {
        OffreEmploi createdOffreEmploi = offreEmploiService.createOffreEmploi(offreEmploi);
        notificationService.createNotification("Nouvelle offre d'emploi ajoutée: " + offreEmploi.getTitre());
        return new ResponseEntity<>(createdOffreEmploi, HttpStatus.CREATED);
    }


    // Endpoint to retrieve all OffreEmplois
    @GetMapping
    public ResponseEntity<List<OffreEmploi>> getAllOffresEmploi() {
        List<OffreEmploi> offresEmploi = offreEmploiService.getAllOffresEmploi();
        return new ResponseEntity<>(offresEmploi, HttpStatus.OK);
    }

    // Endpoint to retrieve a specific OffreEmploi by ID
    @GetMapping("/{id}")
    public ResponseEntity<OffreEmploi> getOffreEmploiById(@PathVariable("id") Long id) {
        Optional<OffreEmploi> offreEmploi = offreEmploiService.getOffreEmploiById(id);
        return offreEmploi.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to update an existing OffreEmploi
    @PutMapping("/{id}")
    public ResponseEntity<OffreEmploi> updateOffreEmploi(@PathVariable("id") Long id, @RequestBody OffreEmploi offreEmploi) {
        OffreEmploi updatedOffreEmploi = offreEmploiService.updateOffreEmploi(id, offreEmploi);
        if (updatedOffreEmploi != null) {
            return new ResponseEntity<>(updatedOffreEmploi, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete an OffreEmploi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffreEmploi(@PathVariable("id") Long id) {
        offreEmploiService.deleteOffreEmploi(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint to apply for a job offer with a CV
    // Endpoint pour postuler à une offre d'emploi avec un CV
    @PostMapping("/{id}/apply")
    public ResponseEntity<String> applyToOffreEmploi(@PathVariable("id") Long id, @RequestParam("cv") MultipartFile cvFile) {
        try {
            // Extraction du texte du fichier CV téléchargé
            InputStream inputStream = cvFile.getInputStream();
            PDDocument document = PDDocument.load(inputStream);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            document.close();

            // Vérification si le CV est en français
            if (!isFrenchCV(text)) {
                return new ResponseEntity<>("Le CV n'est pas en français.", HttpStatus.BAD_REQUEST);
            }

            // Extraction de l'email du CV
            String email = extractEmail(text);
            if (email == null) {
                return new ResponseEntity<>("Aucun email trouvé dans le CV.", HttpStatus.BAD_REQUEST);
            }

            // Récupération de l'offre d'emploi pour vérifier les compétences requises
            Optional<OffreEmploi> offreEmploiOptional = offreEmploiService.getOffreEmploiById(id);
            if (offreEmploiOptional.isEmpty()) {
                return new ResponseEntity<>("Offre d'emploi non trouvée", HttpStatus.NOT_FOUND);
            }

            OffreEmploi offreEmploi = offreEmploiOptional.get();
            String competencesRequises = offreEmploi.getCompetencesRequises().toLowerCase();

            // Vérification du niveau d'éducation requis
            if (!containsRequiredEducationLevel(text.toLowerCase(), offreEmploi.getNiveauEducationRequis())) {
                emailService.sendAcceptanceOrRejectionEmail(email, false);
                return new ResponseEntity<>("Le niveau d'éducation requis n'est pas satisfait.", HttpStatus.BAD_REQUEST);
            }

            // Extraction et vérification de l'expérience requise
            int experience = extractExperience(text);
            if (experience < offreEmploi.getExperienceRequise()) {
                emailService.sendAcceptanceOrRejectionEmail(email, false);
                return new ResponseEntity<>("L'expérience requise n'est pas suffisante.", HttpStatus.BAD_REQUEST);
            }

            // Vérification si le CV contient les compétences requises ou les technologies pertinentes
            if (containsRequiredSkillsOrTechnologies(text.toLowerCase(), competencesRequises)) {
                // Enregistrement de l'email dans un fichier CSV
                saveEmailToCSV(email);

                // Envoi d'un email au candidat pour planifier un entretien
                LocalDateTime interviewDateTime = scheduleInterviewDateTime();
                emailService.sendInterviewInvitation(email, interviewDateTime);

                // Planifier les entretiens pour tous les candidats acceptés
                sendInterviewInvitations();

                return new ResponseEntity<>("Le CV est compatible avec l'offre d'emploi. Un email a été envoyé.", HttpStatus.OK);
            } else {
                emailService.sendAcceptanceOrRejectionEmail(email, false);
                return new ResponseEntity<>("Le CV ne correspond pas aux compétences requises pour l'offre. Un email a été envoyé.", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Échec du traitement du fichier CV", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Échec de l'envoi de l'email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isFrenchCV(String text) {
        String[] frenchKeywords = {"le", "la", "les", "de", "des", "un", "une", "et", "ou", "à"};
        long count = Arrays.stream(frenchKeywords)
                .filter(keyword -> text.toLowerCase().contains(keyword))
                .count();
        return count >= 3;
    }

    private String extractEmail(String text) {
        String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private boolean containsRequiredEducationLevel(String cvText, String niveauEducationRequis) {
        String[] educationKeywords = {"ingénieur", "ingénierie"};
        for (String keyword : educationKeywords) {
            if (cvText.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private int extractExperience(String text) {
        String experienceRegex = "(\\d+)\\s*(ans|années)\\s*(d'?expérience)?";
        Pattern pattern = Pattern.compile(experienceRegex);
        Matcher matcher = pattern.matcher(text);
        int maxExperience = 0;
        while (matcher.find()) {
            int experience = Integer.parseInt(matcher.group(1));
            if (experience > maxExperience) {
                maxExperience = experience;
            }
        }
        return maxExperience;
    }

    private boolean containsRequiredSkillsOrTechnologies(String cvText, String competencesRequises) {
        List<String> generalTechnologies = Arrays.asList(
                "html", "css", "javascript", "typescript", "python", "java", "php", "ruby", "c#", "c++", "swift", "kotlin", "dart", "go", "rust",
                "angular", "react", "vue", "svelte", "bootstrap", "tailwind", "jquery",
                "node.js", "spring boot", "django", "flask", "ruby on rails", "asp.net", "laravel", "symfony", "express.js",
                "next.js", "nuxt.js", "meteor", "blazor",
                "flutter", "react native", "ionic", "swiftui", "jetpack compose", "xamarin", "cordova"
        );

        List<String> requiredSkills = Arrays.asList(competencesRequises.split(",\\s*"));

        List<String> combinedSkills = new ArrayList<>(requiredSkills);
        combinedSkills.addAll(generalTechnologies);

        for (String skill : combinedSkills) {
            if (countOccurrences(cvText, skill) >= 2) {
                return true;
            }
        }
        return false;
    }

    private int countOccurrences(String text, String word) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        return count;
    }

    private LocalDateTime scheduleInterviewDateTime() {
        return LocalDateTime.now().plusWeeks(1).withHour(10).withMinute(0);
    }

    private void saveEmailToCSV(String email) throws IOException {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/emails.csv", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(email);
            bufferedWriter.newLine();
        }
    }

    private List<String> getEmailsFromCSV(String csvFilePath) throws IOException {
        List<String> emails = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(csvFilePath);
             CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream), CSVFormat.DEFAULT.withHeader("Email"))) {
            for (CSVRecord record : csvParser) {
                String email = record.get("Email");
                emails.add(email);
            }
        }
        return emails;
    }

    private void sendInterviewInvitations() {
        try {
            String csvFilePath = "src/main/resources/emails.csv"; // Mettez à jour le chemin du fichier CSV
            List<String> emails = getEmailsFromCSV(csvFilePath);
            LocalDateTime interviewDateTime = scheduleInterviewDateTime();

            for (String email : emails) {
                emailService.sendInterviewInvitation(email, interviewDateTime);
            }
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }




}
