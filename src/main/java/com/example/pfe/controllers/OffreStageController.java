package com.example.pfe.controllers;

import com.example.pfe.models.OffreStage;
import com.example.pfe.services.OffreStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class OffreStageController {

    private final OffreStageService offreStageService;

    @Autowired
    public OffreStageController(OffreStageService offreStageService) {
        this.offreStageService = offreStageService;
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
}
