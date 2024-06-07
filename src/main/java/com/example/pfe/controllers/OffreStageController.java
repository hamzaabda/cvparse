package com.example.pfe.controllers;

import com.example.pfe.models.OffreStage;
import com.example.pfe.services.OffreStageService;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.pfe.email.EmailService;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/auth")
public class OffreStageController {

    private final OffreStageService offreStageService;
    private final Tesseract tesseract;
    private static final Logger logger = LoggerFactory.getLogger(OffreStageController.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    public OffreStageController(OffreStageService offreStageService) {
        this.offreStageService = offreStageService;
        this.tesseract = new Tesseract();

        // Configure le chemin vers le dossier "tessdata" de Tesseract
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
    }

    @PostMapping("/offres-stage")
    public ResponseEntity<OffreStage> createOffreStage(@RequestBody OffreStage offreStage) {
        OffreStage createdOffreStage = offreStageService.createOffreStage(offreStage);
        return new ResponseEntity<>(createdOffreStage, HttpStatus.CREATED);
    }

    @GetMapping("/offres-stage")
    public ResponseEntity<List<OffreStage>> getAllOffresStage() {
        List<OffreStage> offresStage = offreStageService.getAllOffresStage();
        return new ResponseEntity<>(offresStage, HttpStatus.OK);
    }

    @GetMapping("/offres-stage/{id}")
    public ResponseEntity<OffreStage> getOffreStageById(@PathVariable("id") Long id) {
        Optional<OffreStage> offreStage = offreStageService.getOffreStageById(id);
        return offreStage.map(stage -> new ResponseEntity<>(stage, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/offres-stage/{id}")
    public ResponseEntity<OffreStage> updateOffreStage(@PathVariable("id") Long id, @RequestBody OffreStage updatedOffreStage) {
        OffreStage updatedStage = offreStageService.updateOffreStage(id, updatedOffreStage);
        return updatedStage != null ?
                new ResponseEntity<>(updatedStage, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/upload-cv/{offreStageId}")
    public ResponseEntity<String> uploadCVToOffreStage(
            @RequestParam("file") MultipartFile file,
            @PathVariable("offreStageId") Long offreStageId) {

        if (file.isEmpty()) {
            logger.warn("Le fichier est vide");
            return new ResponseEntity<>("Le fichier est vide", HttpStatus.BAD_REQUEST);
        }

        try {
            // Enregistrer temporairement le fichier sur le serveur
            Path tempFilePath = Files.createTempFile("cv-", ".pdf");
            file.transferTo(tempFilePath);
            logger.info("Fichier téléchargé et enregistré temporairement à: {}", tempFilePath);

            // Convertir le fichier PDF en images
            List<BufferedImage> images = convertPdfToImages(tempFilePath);
            logger.debug("Nombre d'images extraites du PDF: {}", images.size());

            // Effectuer la reconnaissance OCR sur chaque image
            StringBuilder extractedText = new StringBuilder();
            for (BufferedImage image : images) {
                String text = tesseract.doOCR(image);
                logger.debug("Texte extrait de l'image : {}", text);
                extractedText.append(text).append("\n");
            }

            // Supprimer le fichier temporaire
            Files.delete(tempFilePath);
            logger.info("Fichier temporaire supprimé");

            // Récupérer l'offre de stage par son ID
            Optional<OffreStage> offreStageOptional = offreStageService.getOffreStageById(offreStageId);
            if (!offreStageOptional.isPresent()) {
                return new ResponseEntity<>("Offre de stage non trouvée", HttpStatus.NOT_FOUND);
            }

            OffreStage offreStage = offreStageOptional.get();

            // Extraire les coordonnées du texte extrait du CV
            Map<String, String> candidateContacts = extractCandidateContactsFromText(extractedText.toString());

            // Envoyer email et enregistrer dans un fichier CSV si les compétences sont adaptées
            if (compareCompetences(Arrays.asList(offreStage.getCompetencesRequises().toLowerCase().split(",\\s*")), extractCompetencesFromText(extractedText.toString()))) {
                String candidateEmail = candidateContacts.get("email");
                String candidatePhone = candidateContacts.get("phone");

                if (candidateEmail != null) {
                    try {
                        emailService.sendAcceptanceOrRejectionEmail(candidateEmail, true);
                        logger.info("Email de confirmation envoyé avec succès à " + candidateEmail);
                    } catch (MessagingException e) {
                        logger.error("Erreur lors de l'envoi de l'email : {}", e.getMessage());
                        return new ResponseEntity<>("Erreur lors de l'envoi de l'email au candidat", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    logger.warn("Email du candidat non trouvé dans le CV");
                    return new ResponseEntity<>("Email du candidat non trouvé dans le CV", HttpStatus.BAD_REQUEST);
                }

                // Enregistrer les informations dans un fichier CSV
                String csvFilePath = "candidate_info.csv";
                try {
                    saveCandidateInfoToCSV(csvFilePath, candidateContacts);
                } catch (IOException e) {
                    logger.error("Erreur lors de l'enregistrement des informations candidat dans le fichier CSV : {}", e.getMessage());
                    return new ResponseEntity<>("Erreur lors de l'enregistrement des informations candidat dans le fichier CSV", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                return new ResponseEntity<>("Les compétences du candidat correspondent à l'offre de stage. Un email a été envoyé au candidat pour informer qu'il est accepté dans le stage.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Les compétences du candidat ne correspondent pas à l'offre de stage", HttpStatus.BAD_REQUEST);
            }

        } catch (IOException | TesseractException e) {
            logger.error("Erreur lors de l'extraction du texte : {}", e.getMessage());
            return new ResponseEntity<>("Erreur lors de l'extraction du texte du CV", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Méthode pour sauvegarder les informations du candidat dans un fichier CSV
    private void saveCandidateInfoToCSV(String csvFilePath, Map<String, String> candidateContacts) throws IOException {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Nom", "Prenom", "Email", "Telephone"});

        // Supposons que les noms et prénoms ne sont pas extraits directement ici
        // Exemple basique pour illustrer l'écriture dans le CSV
        data.add(new String[]{"", "", candidateContacts.get("email"), candidateContacts.get("phone")});

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(csvFilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (String[] line : data) {
                writer.write(String.join(",", line));
                writer.newLine();
            }
        }
    }
    @DeleteMapping("/offres-stage/{id}")
    public ResponseEntity<Void> deleteOffreStage(@PathVariable("id") Long id) {
        offreStageService.deleteOffreStage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OffreStage>> searchOffresStageByTypeAndEducation(
            @RequestParam("typeStage") String typeStage,
            @RequestParam("niveauEducationRequis") String niveauEducationRequis) {
        List<OffreStage> offresStage = offreStageService.searchByTypeAndEducation(typeStage, niveauEducationRequis);
        return new ResponseEntity<>(offresStage, HttpStatus.OK);
    }

    // Méthode pour convertir un PDF en une liste d'images
    private List<BufferedImage> convertPdfToImages(Path pdfPath) throws IOException {
        List<BufferedImage> images = new ArrayList<>();
        try (PDDocument document = PDDocument.load(pdfPath.toFile())) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int pageCount = document.getNumberOfPages();

            // Parcourir les pages du document
            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                // Render la page à 300 DPI pour une meilleure qualité
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 300);
                images.add(image);
            }
        }
        return images;
    }

    // Méthode pour extraire les compétences du texte extrait du CV
    private List<String> extractCompetencesFromText(String text) {
        List<String> competences = new ArrayList<>();
        // Mots-clés pour les compétences
        String[] keywords = {"spring", "nodejs", "java", "python", "sql", "javascript", "c++", "c#", "ruby", "go", "asp.net", "angular", "react", "vue"};

        // Convertir le texte en minuscules pour une recherche insensible à la casse
        String lowerText = text.toLowerCase();

        // Vérifier chaque mot-clé et l'ajouter à la liste si présent dans le texte
        for (String keyword : keywords) {
            if (lowerText.contains(keyword)) {
                competences.add(keyword);
            }
        }
        return competences;
    }

    // Méthode pour comparer les compétences requises et celles du CV
    private boolean compareCompetences(List<String> competencesRequises, List<String> competencesCV) {
        // Vérifier si toutes les compétences requises sont présentes dans les compétences du CV
        return competencesCV.containsAll(competencesRequises);
    }

    // Méthode pour extraire les coordonnées (email et numéro de téléphone) du texte extrait du CV
    private Map<String, String> extractCandidateContactsFromText(String text) {
        Map<String, String> contacts = new HashMap<>();

        // Regex pour extraire l'email
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        Matcher emailMatcher = emailPattern.matcher(text);
        if (emailMatcher.find()) {
            contacts.put("email", emailMatcher.group());
        }

        // Liste d'expressions régulières pour différents formats de numéros de téléphone
        List<Pattern> phonePatterns = Arrays.asList(
                Pattern.compile("(?i)(?:\\b(?:phone|téléphone|tel|☎️|📞)?:?\\s*)?\\+216\\s?\\d{1,3}\\s?\\d{1,3}\\s?\\d{1,4}\\b"), // Format +216 xxxx xxxx ou variantes
                Pattern.compile("\\+?\\d{2,3}?\\s?\\d{1,3}\\s?\\d{2,4}\\s?\\d{2,4}\\s?\\d{2,4}"), // Format international ou local
                Pattern.compile("(?i)(?:\\b(?:phone|téléphone|tel|☎️|📞)?:?\\s*)?\\d{8}") // Format 8 chiffres (téléphone local)
        );

        // Essayer d'extraire le numéro de téléphone avec les différentes expressions régulières
        for (Pattern phonePattern : phonePatterns) {
            Matcher phoneMatcher = phonePattern.matcher(text);
            if (phoneMatcher.find()) {
                contacts.put("phone", phoneMatcher.group());
                break; // Arrêter la recherche si un numéro de téléphone est trouvé
            }
        }

        return contacts;
    }
}