package com.example.pfe.services;

import com.example.pfe.models.ApplicationUser;
import com.example.pfe.models.LoginResponseDTO;
import com.example.pfe.models.Role;
import com.example.pfe.repository.RecruiterRepository;
import com.example.pfe.repository.RoleRepository;
import com.example.pfe.repository.UserRepository;
import com.example.pfe.email.EmailService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private EmailService emailService;

    @Autowired
    private RecruiterRepository recruiterRepository ;

    public ApplicationUser registerUser(String username, String password, String email) {
        // Vérifie si l'email existe déjà
        Optional<ApplicationUser> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").orElseThrow(() -> new IllegalStateException("User role not found"));

        ApplicationUser newUser = userRepository.save(new ApplicationUser(0, username, encodedPassword, Collections.singleton(userRole), email));

        // Envoyer l'e-mail de confirmation d'inscription
        try {
            emailService.sendConfirmationEmail(email);
        } catch (MessagingException e) {
            // Gérer l'échec de l'envoi de l'e-mail
            e.printStackTrace();
        }

        return newUser;
    }

    public LoginResponseDTO loginUser(String email, String password) {
        // Recherche de l'utilisateur par son email
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        // Vérification si le mot de passe correspond
        if (passwordEncoder.matches(password, user.getPassword())) {
            // Construction de l'objet d'authentification
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getEmail(), password,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            );

            // Génération du token JWT
            String token = tokenService.generateJwt(authentication);

            // Création du DTO de réponse avec l'utilisateur et le token
            return new LoginResponseDTO(user, token);
        } else {
            // Gérer le cas où le mot de passe est incorrect
            throw new IllegalArgumentException("Incorrect password");
        }
    }

    public void logout() {
        SecurityContextHolder.clearContext();

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

    @PostConstruct
    public void initDefaultAdmin() {
        if (!roleRepository.findByAuthority("ADMIN").isPresent()) {
            Role adminRole = roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("USER"));

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            // Create a user with necessary parameters including email
            ApplicationUser admin = new ApplicationUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setAuthorities(roles);
            admin.setEmail("admin@example.com");

            userRepository.save(admin);
        }
    }

    public long getTotalUsersCount() {
        // Retrieve the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object contains the JWT token
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

            // Extract the Jwt object from the authentication token
            Jwt jwt = jwtAuthenticationToken.getToken();

            // Extract necessary user information from the Jwt
            String username = jwt.getSubject();

            // Extract the isAdmin claim from the Jwt
            Boolean isAdmin = jwt.getClaim("isAdmin");

            // Check if the isAdmin claim exists and has a non-null value
            if (isAdmin != null && isAdmin.booleanValue()) {
                // Logic to get total users count
                return userRepository.count();
            } else {
                // If the user is not admin, throw a RuntimeException indicating unauthorized access
                throw new RuntimeException("User is not authorized to perform this action");
            }
        } else {
            // If the authentication is not a JWT token, throw a RuntimeException indicating unauthorized access
            throw new RuntimeException("User is not authorized to perform this action");
        }
    }

    public void addAdmin(String username, String password, String email) {
        // Vérifie si l'email existe déjà
        Optional<ApplicationUser> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Récupérer le rôle ADMIN
        Role adminRole = roleRepository.findByAuthority("ADMIN")
                .orElseThrow(() -> new IllegalStateException("Admin role not found"));

        // Créer un nouvel administrateur avec le rôle ADMIN
        ApplicationUser newAdmin = new ApplicationUser();
        newAdmin.setUsername(username);
        newAdmin.setPassword(passwordEncoder.encode(password));
        newAdmin.setEmail(email);
        newAdmin.setAuthorities(Collections.singleton(adminRole));

        // Enregistrer le nouvel administrateur
        userRepository.save(newAdmin);

        // Envoyer un e-mail de confirmation
        try {
            emailService.sendConfirmationEmail(email);
        } catch (MessagingException e) {
            // Gérer l'échec de l'envoi de l'e-mail
            e.printStackTrace();
        }
    }

    public void blockAdmin(Integer adminId) {
        // Convertir l'identifiant en Long
        Long adminIdLong = adminId.longValue();

        // Trouver l'administrateur par son identifiant
        Optional<ApplicationUser> adminOptional = userRepository.findById(Math.toIntExact(adminIdLong));

        // Vérifier si l'administrateur existe
        if (adminOptional.isPresent()) {
            ApplicationUser admin = adminOptional.get();

            // Révoquer toutes les autorisations de l'administrateur
            admin.setAuthorities(Collections.emptySet());

            // Mettre à jour l'administrateur dans la base de données
            userRepository.save(admin);

            // Autres actions si nécessaire, comme envoyer un e-mail de notification, etc.
        } else {
            throw new IllegalArgumentException("Admin not found with ID: " + adminId);
        }
    }



    public List<ApplicationUser> getAllUsers() {
        return userRepository.findAll();
    }

    // Méthode pour compter le nombre de connexions réussies par jour
    public long getSuccessfulLoginsCountByDay(Date date) {
        // Implémentez cette méthode pour compter le nombre de connexions réussies pour une journée spécifique
        return userRepository.countSuccessfulLoginsByDay(date);
    }

    // Méthode pour compter le nombre de réinitialisations de mot de passe par mois
    public long getPasswordResetsCountByMonth(int year, int month) {
        // Implémentez cette méthode pour compter le nombre de réinitialisations de mot de passe pour un mois spécifique
        return userRepository.countPasswordResetsByMonth(year, month);
    }

    // Méthode pour compter le nombre d'utilisateurs inscrits par mois
    public long getRegisteredUsersCountByMonth(int year, int month) {
        // Implémentez cette méthode pour compter le nombre d'utilisateurs inscrits pour un mois spécifique
        return userRepository.countRegisteredUsersByMonth(year, month);
    }


    public List<ApplicationUser> searchAdminsByUsernameOrEmail(String keyword) {
        // Recherche les administrateurs par nom d'utilisateur ou e-mail
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseAndAuthorities(
                keyword, keyword, roleRepository.findByAuthority("ADMIN").orElseThrow(() -> new IllegalStateException("Admin role not found"))
        );
    }



}
