package com.example.pfe.controllers;

import com.example.pfe.models.OffreStage;
import com.example.pfe.services.OffreStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
public class OffreStageController {

    private final OffreStageService offreStageService;

    @Autowired
    public OffreStageController(OffreStageService offreStageService) {
        this.offreStageService = offreStageService;
    }

    // Nouvelle méthode pour ajouter une nouvelle offre de stage
    @PostMapping("/offres-stage")
    public ResponseEntity<OffreStage> createOffreStage(@RequestBody OffreStage offreStage) {
        // Appeler le service pour créer une nouvelle offre de stage
        OffreStage createdOffreStage = offreStageService.createOffreStage(offreStage);

        // Retourner une réponse HTTP avec l'offre de stage créée
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



    // Endpoint pour postuler à une offre de stage et télécharger un CV
    @PostMapping("/postuler/{idOffre}")
    public ResponseEntity<String> postulerOffreStage(@PathVariable Long idOffre, @RequestParam("cv") List<MultipartFile> cvs) {
        // Vérifiez si l'offre de stage existe
        Optional<OffreStage> offreStageOptional = offreStageService.getOffreStageById(idOffre);
        if (offreStageOptional.isPresent()) {
            OffreStage offreStage = offreStageOptional.get();

            // Traitez chaque CV individuellement
            for (MultipartFile cv : cvs) {
                // Vérifiez si le fichier CV est au format PDF
                if (!cv.getContentType().equals("application/pdf")) {
                    return ResponseEntity.badRequest().body("Le CV doit être au format PDF.");
                }

                try {
                    // Sauvegarde du CV dans un dossier (ou tout autre système de stockage approprié)
                    String fileName = StringUtils.cleanPath(cv.getOriginalFilename());
                    Path path = Paths.get(fileName);
                    Files.copy(cv.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    // Ajoutez le nom du fichier CV à la liste des CVs de l'offre de stage
                    List<String> offreStageCvs = offreStage.getCvs();
                    offreStageCvs.add(fileName); // Ajoutez le nom du fichier à la liste des CVs

                    // Mettez à jour l'offre de stage avec la nouvelle liste de CVs
                    offreStage.setCvs(offreStageCvs);

                    // Enregistrez l'offre de stage mise à jour dans la base de données
                    offreStageService.updateOffreStage(idOffre, offreStage);

                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'enregistrement du CV.");
                }
            }

            return ResponseEntity.ok("Candidature soumise avec succès pour l'offre de stage avec l'ID : " + idOffre);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint pour télécharger tous les CVs associés à une offre de stage
    @GetMapping("/offres-stage/{id}/download")
    public ResponseEntity<Resource> downloadCvsForOffreStage(@PathVariable("id") Long id) {
        // Vérifiez si l'offre de stage existe
        Optional<OffreStage> offreStageOptional = offreStageService.getOffreStageById(id);
        if (offreStageOptional.isPresent()) {
            OffreStage offreStage = offreStageOptional.get();

            // Récupérez la liste des noms de fichiers des CVs associés à cette offre de stage
            List<String> cvs = offreStage.getCvs();

            // Vérifiez s'il y a des CVs associés à cette offre de stage
            if (cvs.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // Créez une archive ZIP pour regrouper tous les CVs
            Path zipFile = Paths.get("cvs.zip");
            try {
                // Créez une archive ZIP
                Files.deleteIfExists(zipFile);
                Files.createFile(zipFile);
                java.util.zip.ZipOutputStream zipOutputStream = new java.util.zip.ZipOutputStream(Files.newOutputStream(zipFile));

                // Ajoutez chaque CV à l'archive ZIP
                for (String cvFileName : cvs) {
                    Path cvPath = Paths.get(cvFileName);
                    java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(cvFileName);
                    zipOutputStream.putNextEntry(zipEntry);
                    Files.copy(cvPath, zipOutputStream);
                }

                zipOutputStream.close();

                // Téléchargez l'archive ZIP
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(zipFile));

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cvs.zip");

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(resource.contentLength())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}



