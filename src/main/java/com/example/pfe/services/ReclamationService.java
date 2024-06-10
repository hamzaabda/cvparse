package com.example.pfe.services;

import com.example.pfe.models.Reclamation;

import com.example.pfe.repository.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;

    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    public Optional<Reclamation> getReclamationById(Long id) {
        return reclamationRepository.findById(id);
    }

    public Reclamation createReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    public Reclamation updateReclamation(Long id, Reclamation newReclamation) {
        if (reclamationRepository.existsById(id)) {
            newReclamation.setId(id);
            return reclamationRepository.save(newReclamation);
        } else {
            throw new RuntimeException("Reclamation not found with id: " + id);
        }
    }

    public void deleteReclamation(Long id) {
        reclamationRepository.deleteById(id);
    }
}
