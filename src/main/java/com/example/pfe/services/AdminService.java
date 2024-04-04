package com.example.pfe.services;

import com.example.pfe.email.EmailService;
import com.example.pfe.models.Recruiter;
import com.example.pfe.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;

@Service
@Transactional
public class AdminService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationService authenticationService;

    public void addRecruiter(Recruiter recruiter) {
        recruiterRepository.save(recruiter);

        // Envoyer un e-mail de confirmation au recruteur avec un lien pour définir son mot de passe initial
        try {
            emailService.sendConfirmationEmail(recruiter.getEmail());
        } catch (MessagingException e) {
            // Gérer l'échec de l'envoi de l'e-mail
            e.printStackTrace();
        }
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


    // Méthode pour afficher tous les recruteurs
    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }
    public List<Recruiter> searchRecruitersByEmail(String email) {
        return recruiterRepository.findByEmailContaining(email);
    }

}

