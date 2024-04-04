package com.example.pfe.controllers;

import com.example.pfe.models.OffreEmploi;
import com.example.pfe.services.OffreEmploiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class OffreEmploiController {

    @Autowired
    private OffreEmploiService offreEmploiService;

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


}
