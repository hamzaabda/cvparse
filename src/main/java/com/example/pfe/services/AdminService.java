package com.example.pfe.services;

import com.example.pfe.models.Recruiter;
import com.example.pfe.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    public void addRecruiter(Recruiter recruiter) {
        recruiterRepository.save(recruiter);
    }

    public void updateRecruiter(Long recruiterId, Recruiter recruiterDetails) {
        // Vérifier si le recruteur existe dans la base de données
        Recruiter existingRecruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new IllegalArgumentException("Recruiter not found"));

        // Mettre à jour les détails du recruteur
        existingRecruiter.setUsername(recruiterDetails.getUsername());
        existingRecruiter.setEmail(recruiterDetails.getEmail());
        // Vous pouvez ajouter d'autres champs à mettre à jour ici

        // Enregistrer les modifications dans la base de données
        recruiterRepository.save(existingRecruiter);
    }

    public void deleteRecruiter(Long recruiterId) {
        // Vérifier si le recruteur existe dans la base de données
        Recruiter existingRecruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new IllegalArgumentException("Recruiter not found"));

        // Supprimer le recruteur de la base de données
        recruiterRepository.delete(existingRecruiter);
    }


}
