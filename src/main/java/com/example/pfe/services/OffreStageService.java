package com.example.pfe.services;

import com.example.pfe.models.OffreStage;

import com.example.pfe.repository.OffreStageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OffreStageService {

    private final OffreStageRepository offreStageRepository;

    @Autowired
    public OffreStageService(OffreStageRepository offreStageRepository) {
        this.offreStageRepository = offreStageRepository;
    }

    public OffreStage createOffreStage(OffreStage offreStage) {
        // ... (vérifications existantes)

        // Calcul de la date d'expiration (un mois à partir de la date actuelle)
        LocalDate now = LocalDate.now();
        offreStage.setDateExpiration(now.plusMonths(1));

        // Sauvegarde de l'offre
        return offreStageRepository.save(offreStage);
    }


    public List<OffreStage> getAllOffresStage() {
        return offreStageRepository.findAll();
    }

    public Optional<OffreStage> getOffreStageById(Long id) {
        return offreStageRepository.findById(id);
    }

    public OffreStage updateOffreStage(Long id, OffreStage updatedOffreStage) {
        if (offreStageRepository.existsById(id)) {
            updatedOffreStage.setId(id);
            return offreStageRepository.save(updatedOffreStage);
        } else {
            // Gérer l'exception si l'offre de stage n'existe pas
            return null;
        }
    }

    public void deleteOffreStage(Long id) {
        offreStageRepository.deleteById(id);
    }
    public List<OffreStage> searchByTypeAndEducation(String typeStage, String niveauEducationRequis) {
        return offreStageRepository.findByTypeStageAndNiveauEducationRequis(typeStage, niveauEducationRequis);
    }

}
