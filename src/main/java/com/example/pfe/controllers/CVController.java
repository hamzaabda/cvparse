package com.example.pfe.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth/cv")
public class CVController {

    private static final Logger logger = LoggerFactory.getLogger(CVController.class);

    private final Tesseract tesseract;

    public CVController() {
        tesseract = new Tesseract();
        // Assurez-vous de spécifier le chemin du dossier de données de Tesseract
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        // Vous pouvez spécifier la langue ici (par exemple, "eng" pour l'anglais, "fra" pour le français)
        tesseract.setLanguage("eng+fra"); // Pour les langues anglaise et française
    }

    @PostMapping("/analyze")
    public Map<String, String> analyzeCV(@RequestParam("file") MultipartFile file,
                                         @RequestParam(value = "language", defaultValue = "eng") String language) {
        Map<String, String> response = new HashMap<>();
        if (file.isEmpty() || !isValidFileFormat(file.getOriginalFilename())) {
            response.put("error", "Invalid file type. Please upload an image or PDF file.");
            return response;
        }

        // Configurer Tesseract pour la langue
        tesseract.setLanguage(language);

        try {
            String result = "";
            if (file.getContentType().equals("application/pdf")) {
                result = extractTextFromPDF(file.getInputStream());
            } else {
                // Convertir MultipartFile en fichier temporaire pour les images
                File tempFile = File.createTempFile("temp", ".png");
                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    fos.write(file.getBytes());
                }
                // Convertir le fichier temporaire en texte via OCR
                result = tesseract.doOCR(tempFile);
                tempFile.delete(); // Supprimer le fichier temporaire après l'utilisation
            }

            logger.info("Text extracted from CV: {}", result);

            // Analyser les données extraites et proposer des corrections
            String corrections = analyzeAndSuggestCorrections(result, language);

            response.put("extractedText", result);
            response.put("corrections", corrections);
        } catch (IOException e) {
            logger.error("IOException occurred while processing the CV", e);
            response.put("error", "Failed to process the CV due to an I/O error.");
        } catch (TesseractException e) {
            logger.error("TesseractException occurred while performing OCR", e);
            response.put("error", "Failed to perform OCR on the CV.");
        }
        return response;
    }

    private boolean isValidFileFormat(String filename) {
        // Vérifiez si le fichier est une image ou un PDF valide
        return filename != null && (filename.endsWith(".png") || filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".pdf"));
    }

    private String extractTextFromPDF(InputStream pdfInputStream) throws IOException {
        try (PDDocument document = PDDocument.load(pdfInputStream)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

    private String analyzeAndSuggestCorrections(String text, String language) {
        StringBuilder corrections = new StringBuilder();

        // Exemple de correction orthographique
        corrections.append(checkSpelling(text, language));

        // Exemple de validation de format
        corrections.append(validateFormat(text));

        // Exemple de détection de contenu manquant
        corrections.append(detectMissingContent(text, language));

        // Autres corrections spécifiques
        // ...

        return corrections.toString();
    }

    private String checkSpelling(String text, String language) {
        StringBuilder corrections = new StringBuilder();
        String[] words = text.split("\\s+");
        LevenshteinDistance levenshtein = new LevenshteinDistance();

        // Liste d'exemples de mots incorrects pour différentes langues
        String[] commonMisspellings;
        if (language.equals("fra")) {
            commonMisspellings = new String[]{"mistkae", "adress", "experiance"};
        } else {
            commonMisspellings = new String[]{"mistake", "address", "experience"};
        }

        for (String word : words) {
            for (String misspelled : commonMisspellings) {
                if (levenshtein.apply(word.toLowerCase(), misspelled) <= 2) {
                    corrections.append(String.format("Correction: '%s' should be '%s'.\n", word, misspelled));
                }
            }
        }
        return corrections.toString();
    }

    private String validateFormat(String text) {
        StringBuilder corrections = new StringBuilder();

        // Validation des e-mails
        Pattern emailPattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
        Matcher emailMatcher = emailPattern.matcher(text);
        while (emailMatcher.find()) {
            String email = emailMatcher.group();
            if (!isValidEmail(email)) {
                corrections.append(String.format("Invalid email format detected: '%s'.\n", email));
            }
        }

        // Validation des dates (format YYYY-MM-DD)
        Pattern datePattern = Pattern.compile("\\b\\d{4}-\\d{2}-\\d{2}\\b");
        Matcher dateMatcher = datePattern.matcher(text);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (dateMatcher.find()) {
            String date = dateMatcher.group();
            try {
                LocalDate.parse(date, dateFormatter);
            } catch (DateTimeParseException e) {
                corrections.append(String.format("Invalid date format detected: '%s'.\n", date));
            }
        }

        return corrections.toString();
    }

    private boolean isValidEmail(String email) {
        // Utilisation d'une expression régulière simple pour la validation de l'email
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private String detectMissingContent(String text, String language) {
        StringBuilder corrections = new StringBuilder();

        // Vérifiez la présence de sections importantes comme "Expérience", "Formation", "Compétences"
        if (language.equals("fra")) {
            if (!text.toLowerCase().contains("expérience")) {
                corrections.append("Missing section: 'Expérience' not found.\n");
            }
            if (!text.toLowerCase().contains("formation")) {
                corrections.append("Missing section: 'Formation' not found.\n");
            }
            if (!text.toLowerCase().contains("compétences")) {
                corrections.append("Missing section: 'Compétences' not found.\n");
            }
        } else {
            if (!text.toLowerCase().contains("experience")) {
                corrections.append("Missing section: 'Experience' not found.\n");
            }
            if (!text.toLowerCase().contains("education")) {
                corrections.append("Missing section: 'Education' not found.\n");
            }
            if (!text.toLowerCase().contains("skills")) {
                corrections.append("Missing section: 'Skills' not found.\n");
            }
        }

        return corrections.toString();
    }
}
