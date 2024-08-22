package com.example.pfe.controllers;

import com.example.pfe.email.EmailService;
import com.example.pfe.models.Reclamation;
import com.example.pfe.services.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/auth/reclamations")
public class ReclamationController {

    @Autowired
    private ReclamationService reclamationService;
    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<Reclamation> getAllReclamations() {
        return reclamationService.getAllReclamations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reclamation> getReclamationById(@PathVariable Long id) {
        return reclamationService.getReclamationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reclamation> createReclamation(@RequestBody Reclamation reclamation) {
        Reclamation createdReclamation = reclamationService.createReclamation(reclamation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReclamation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reclamation> updateReclamation(@PathVariable Long id, @RequestBody Reclamation reclamation) {
        Reclamation updatedReclamation = reclamationService.updateReclamation(id, reclamation);
        return ResponseEntity.ok(updatedReclamation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable Long id) {
        reclamationService.deleteReclamation(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/reply")
    public ResponseEntity<String> replyToReclamation(@PathVariable Long id, @RequestBody String responseMessage) {
        return reclamationService.getReclamationById(id).map(reclamation -> {
            try {
                emailService.sendReclamationResponse(reclamation.getEmail(), responseMessage);
                return ResponseEntity.ok("Response sent successfully");
            } catch (MessagingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send response");
            }
        }).orElse(ResponseEntity.notFound().build());
    }

}
