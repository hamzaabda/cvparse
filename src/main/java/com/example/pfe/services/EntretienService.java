package com.example.pfe.services;

import com.example.pfe.email.EmailService;
import com.example.pfe.models.Entretien;
import com.example.pfe.repository.EntretienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EntretienService {

    @Autowired
    private EntretienRepository entretienRepository;

    @Autowired
    private EmailService emailService;

    public List<Entretien> getAllEntretiens() {
        return entretienRepository.findAll();
    }

    public Optional<Entretien> getEntretienById(Long id) {
        return entretienRepository.findById(id);
    }

    public Entretien addEntretien(Entretien entretien) {
        return entretienRepository.save(entretien);
    }

    public Entretien updateEntretien(Long id, Entretien entretien) {
        if (entretienRepository.existsById(id)) {
            entretien.setId(id);
            return entretienRepository.save(entretien);
        }
        return null;
    }

    public void deleteEntretien(Long id) {
        entretienRepository.deleteById(id);
    }

    public Entretien acceptEntretien(Long id, LocalDateTime dateHeureEntretien) {
        Optional<Entretien> optionalEntretien = entretienRepository.findById(id);
        if (optionalEntretien.isPresent()) {
            Entretien entretien = optionalEntretien.get();
            entretien.setDateHeure(dateHeureEntretien.toString());
            entretienRepository.save(entretien);

            try {
                emailService.sendInterviewRescheduledEmail(entretien.getEmail(), dateHeureEntretien); // Nouvelle méthode appelée ici
            } catch (Exception e) {
                // Gérer l'exception, par exemple en journalisant l'erreur
                e.printStackTrace();
            }

            return entretien;
        }
        return null;
    }
}
