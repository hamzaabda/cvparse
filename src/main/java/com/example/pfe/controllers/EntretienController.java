package com.example.pfe.controllers;

import com.example.pfe.models.Entretien;
import com.example.pfe.services.EntretienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth/entretiens")
public class EntretienController {

    @Autowired
    private EntretienService entretienService;


    @GetMapping("/{id}")
    public ResponseEntity<Entretien> getEntretienById(@PathVariable Long id) {
        Optional<Entretien> entretien = entretienService.getEntretienById(id);
        return entretien.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Entretien> addEntretien(@RequestBody Entretien entretien) {
        Entretien savedEntretien = entretienService.addEntretien(entretien);
        return new ResponseEntity<>(savedEntretien, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entretien> updateEntretien(@PathVariable Long id, @RequestBody Entretien entretien) {
        Entretien updatedEntretien = entretienService.updateEntretien(id, entretien);
        return updatedEntretien != null ? new ResponseEntity<>(updatedEntretien, HttpStatus.OK)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntretien(@PathVariable Long id) {
        entretienService.deleteEntretien(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Entretien>> getAllEntretiens() {
        List<Entretien> entretiens = entretienService.getAllEntretiens();
        return new ResponseEntity<>(entretiens, HttpStatus.OK);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Entretien> acceptEntretien(@PathVariable Long id, @RequestParam String dateHeure) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateHeureEntretien = LocalDateTime.parse(dateHeure, formatter);

        Entretien entretien = entretienService.acceptEntretien(id, dateHeureEntretien);
        return entretien != null ? new ResponseEntity<>(entretien, HttpStatus.OK)
                : ResponseEntity.notFound().build();
    }
}
