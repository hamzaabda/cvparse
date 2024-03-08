package com.example.pfe.services;

import com.example.pfe.models.ApplicationUser;
import com.example.pfe.models.LoginResponseDTO;
import com.example.pfe.models.Role;
import com.example.pfe.repository.RoleRepository;
import com.example.pfe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return userRepository.save(new ApplicationUser(0, username, encodedPassword, authorities, email));
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

}
