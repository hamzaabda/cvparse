package com.example.pfe.controllers;

import com.example.pfe.models.OffreEmploi;
import com.example.pfe.services.OffreEmploiService;
import com.example.pfe.email.EmailService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class OffreEmploiController {

    @Autowired
    private OffreEmploiService offreEmploiService;

    @Autowired
    private EmailService emailService;

    // Endpoint to create a new OffreEmploi
    @PostMapping
    public ResponseEntity<OffreEmploi> createOffreEmploi(@RequestBody OffreEmploi offreEmploi) {
        OffreEmploi createdOffreEmploi = offreEmploiService.createOffreEmploi(offreEmploi);
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
    @PostMapping("/{id}/apply")
    public ResponseEntity<String> applyToOffreEmploi(@PathVariable("id") Long id, @RequestParam("cv") MultipartFile cvFile) {
        try {
            // Extract text from the uploaded CV file
            InputStream inputStream = cvFile.getInputStream();
            PDDocument document = PDDocument.load(inputStream);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            document.close();

            // Extract email from CV
            String email = extractEmail(text);
            if (email == null) {
                return new ResponseEntity<>("Aucun email trouvé dans le CV.", HttpStatus.BAD_REQUEST);
            }

            // Retrieve the job offer to check the required skills
            Optional<OffreEmploi> offreEmploiOptional = offreEmploiService.getOffreEmploiById(id);
            if (offreEmploiOptional.isEmpty()) {
                return new ResponseEntity<>("Offre d'emploi non trouvée", HttpStatus.NOT_FOUND);
            }

            OffreEmploi offreEmploi = offreEmploiOptional.get();
            String competencesRequises = offreEmploi.getCompetencesRequises().toLowerCase();

            // Check if CV contains required skills
            if (containsRequiredSkills(text.toLowerCase(), competencesRequises)) {
                // Send an email to the candidate
                LocalDate interviewDate = LocalDate.now().plusWeeks(1); // Example: Interview in a week
                emailService.sendInterviewInvitation(email, interviewDate);
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

    private String extractEmail(String text) {
        String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(emailRegex);
        java.util.regex.Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private boolean containsRequiredSkills(String cvText, String competencesRequises) {
        // Extract the required skills from the job offer
        List<String> requiredSkills = Arrays.asList(competencesRequises.split(",\\s*"));

        // Check occurrences for each required skill
        for (String skill : requiredSkills) {
            // Check for at least 2 occurrences of the skill in the text
            if (countOccurrences(cvText, skill) >= 2) {
                return true;
            }
        }
        return false;
    }

    private int countOccurrences(String text, String word) {
        int count = 0;
        int index = 0;
        // Count occurrences of the word in the text
        while ((index = text.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        return count;
    }
}
