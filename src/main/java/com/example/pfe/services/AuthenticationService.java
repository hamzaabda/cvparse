package com.example.pfe.services;

import com.example.pfe.models.ApplicationUser;
import com.example.pfe.models.LoginResponseDTO;
import com.example.pfe.models.Role;
import com.example.pfe.repository.RoleRepository;
import com.example.pfe.repository.UserRepository;
import com.example.pfe.email.EmailService; // Importez votre classe EmailService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService; // Injectez  service EmailService





    public ApplicationUser registerUser(String username, String password, String email){
        // Vérifie si l'email existe déjà
        Optional<ApplicationUser> existingUser = userRepository.findByEmail(email);
        if(existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        ApplicationUser newUser = userRepository.save(new ApplicationUser(0, username, encodedPassword, authorities, email));

        // Envoyer l'e-mail de confirmation d'inscription
        try {
            emailService.sendConfirmationEmail(email);
        } catch (MessagingException e) {
            // Gérer l'échec de l'envoi de l'e-mail (enregistrez les erreurs dans les logs, par exemple)
            e.printStackTrace();
        }

        return newUser;
    }

    public LoginResponseDTO loginUser(String email, String password) {

        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            // Construct authentication object
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), password,
                    Set.of(new SimpleGrantedAuthority("ROLE_USER"))
            );

            String token = tokenService.generateJwt(authentication);
            return new LoginResponseDTO(user, token);
        } else {
            // Handle incorrect password
            throw new IllegalArgumentException("Incorrect password");
        }
    }


    @Transactional
    public void resetPassword(String email) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Générer un nouveau mot de passe aléatoire
        String newPassword = generateRandomPassword();

        // Mettre à jour le mot de passe de l'utilisateur dans la base de données
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Envoyer le nouveau mot de passe par e-mail
        try {
            emailService.sendResetPasswordEmail(email, newPassword);
        } catch (MessagingException e) {
            // Gérer l'échec de l'envoi de l'e-mail
            e.printStackTrace();
        }
    }

    private String generateRandomPassword() {
        // Définir les caractères autorisés pour le mot de passe
        String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+";

        // Définir la longueur du mot de passe
        int passwordLength = 10;

        // Créer un objet StringBuilder pour construire le mot de passe
        StringBuilder password = new StringBuilder(passwordLength);

        // Créer une instance de SecureRandom pour générer des nombres aléatoires sécurisés
        SecureRandom random = new SecureRandom();

        // Générer le mot de passe caractère par caractère
        for (int i = 0; i < passwordLength; i++) {
            // Sélectionner un caractère aléatoire parmi les caractères autorisés
            int randomIndex = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);

            // Ajouter le caractère sélectionné au mot de passe
            password.append(randomChar);
        }

        // Retourner le mot de passe généré
        return password.toString();
    }





}
