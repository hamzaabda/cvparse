package com.example.pfe.services;

import com.example.pfe.models.OffreEmploi;

import com.example.pfe.repository.OffreEmploiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OffreEmploiService {

    @Autowired
    private OffreEmploiRepository offreEmploiRepository;

    @Autowired
    private AuthenticationService authenticationService;

    // Create operation
    public OffreEmploi createOffreEmploi(OffreEmploi offreEmploi) {
        return offreEmploiRepository.save(offreEmploi);
    }

    // Read operation
    public List<OffreEmploi> getAllOffresEmploi() {
        return offreEmploiRepository.findAll();
    }

    public Optional<OffreEmploi> getOffreEmploiById(Long id) {
        return offreEmploiRepository.findById(id);
    }

    // Update operation
    public OffreEmploi updateOffreEmploi(Long id, OffreEmploi offreEmploi) {
        // Check if the offreEmploi with the given ID exists
        if (offreEmploiRepository.existsById(id)) {
            // Setting the ID of the offreEmploi passed in the parameter
            offreEmploi.setId(id);
            return offreEmploiRepository.save(offreEmploi);
        } else {
            // Handle case where offreEmploi with the given ID doesn't exist
            return null; // Or throw an exception as per your requirement
        }
    }

    // Delete operation
    public void deleteOffreEmploi(Long id) {
        offreEmploiRepository.deleteById(id);
    }
}

