package com.example.pfe.services;

import com.example.pfe.models.Profile;
import com.example.pfe.repository.ProfileRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    private Tesseract tesseract;

    public ProfileService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // Changez le chemin selon votre installation
    }

    // Ajoutez ceci à ProfileService
    public Profile createProfileFromCV(MultipartFile file) {
        try {
            // Assurez-vous que le fichier est un PDF
            if (!file.getOriginalFilename().endsWith(".pdf")) {
                throw new IllegalArgumentException("Le fichier doit être un PDF");
            }

            // Convertir le PDF en image et extraire le texte
            String extractedText = convertPdfToImage(file);
            // Extraire les données de profil à partir du texte
            Profile profile = extractProfileData(extractedText);
            // Sauvegarder le profil dans la base de données
            return profileRepository.save(profile); // Retournez le profil sauvegardé
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la création du profil à partir du CV : " + e.getMessage(), e);
        }
    }

    public String convertPdfToImage(MultipartFile file) throws IOException {
        // Créez un fichier temporaire pour le PDF
        File tempFile = File.createTempFile("tempCV", ".pdf");
        file.transferTo(tempFile); // Cela peut également lancer une IOException

        StringBuilder extractedText = new StringBuilder();
        try (PDDocument document = PDDocument.load(tempFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300);
                // Enregistrez l'image ou traitez-la comme vous le souhaitez
                File outputFile = new File("output-page-" + (page + 1) + ".png");
                ImageIO.write(bim, "PNG", outputFile);

                // Extraire le texte à partir de l'image
                String text = extractTextFromImage(outputFile);
                extractedText.append(text).append("\n");

                // Supprimez l'image temporaire après utilisation
                outputFile.delete();
            }
        } finally {
            // Supprimez le fichier temporaire après utilisation
            tempFile.delete();
        }

        return extractedText.toString();
    }

    private String extractTextFromImage(File imageFile) {
        try {
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            throw new RuntimeException("Erreur lors de l'extraction du texte de l'image : " + e.getMessage(), e);
        }
    }

    private Profile extractProfileData(String extractedText) {
        Profile profile = new Profile();
        profile.setFullName(truncate(extractFullName(extractedText), 255));
        profile.setEmail(truncate(extractEmail(extractedText), 255));
        profile.setPhoneNumber(truncate(extractPhoneNumber(extractedText), 50));
        profile.setEducation(truncate(extractEducation(extractedText), 2000));
        profile.setExperience(truncate(extractExperience(extractedText), 2000));
        profile.setSkills(truncate(extractSkills(extractedText), 2000));

        return profile;
    }

    private String truncate(String value, int maxLength) {
        if (value.length() > maxLength) {
            return value.substring(0, maxLength); // Tronque la valeur
        }
        return value; // Retourne la valeur originale si elle ne dépasse pas la longueur
    }

    private String extractFullName(String text) {
        String[] lines = text.split("\n");

        for (String line : lines) {
            if (line.matches("^[A-Z][a-z]+\\s[A-Z][a-z]+.*")) {
                return line.trim();
            }
        }
        return "Nom non trouvé";
    }

    private String extractEmail(String text) {
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}");
        Matcher matcher = emailPattern.matcher(text);

        return matcher.find() ? matcher.group() : "Email non trouvé";
    }

    private String extractPhoneNumber(String text) {
        Pattern phonePattern = Pattern.compile("(\\+\\d{1,3})?\\s?(\\d{1,4}[\\s.-]?){6,}");
        Matcher matcher = phonePattern.matcher(text);

        return matcher.find() ? matcher.group().trim() : "Numéro de téléphone non trouvé";
    }

    private String extractEducation(String text) {
        Pattern educationPattern = Pattern.compile("(?i)(Bachelor|Master|Licence|Diploma|BSc|MSc|PhD)[^\\n]*");
        Matcher matcher = educationPattern.matcher(text);

        StringBuilder education = new StringBuilder();
        while (matcher.find()) {
            education.append(matcher.group()).append("\n");
        }

        return education.length() > 0 ? education.toString().trim() : "Éducation non trouvée";
    }

    private String extractExperience(String text) {
        Pattern experiencePattern = Pattern.compile("(?i)(\\d+\\s+years?|ans?\\s+d’expérience?|expérience?)[^\\n]*");
        Matcher matcher = experiencePattern.matcher(text);

        StringBuilder experience = new StringBuilder();
        while (matcher.find()) {
            experience.append(matcher.group()).append("\n");
        }

        return experience.length() > 0 ? experience.toString().trim() : "Expérience non trouvée";
    }

    private String extractSkills(String text) {
        Pattern skillsPattern = Pattern.compile("(?i)(Skills|Compétences)[^\\n]*\\n([^\\n]*\\n)+");
        Matcher matcher = skillsPattern.matcher(text);

        return matcher.find() ? matcher.group().trim() : "Compétences non trouvées";
    }


    public Profile getProfileById(Long id) {
        return profileRepository.findById(id).orElse(null); // Renvoie null si le profil n'est pas trouvé
    }
}
