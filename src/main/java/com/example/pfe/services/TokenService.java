package com.example.pfe.services;


import com.example.pfe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private UserRepository userRepository;

    public String generateJwt(Authentication auth){

        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        String username = auth.getName();

        // Vérifier si l'utilisateur est l'administrateur prédéfini
        boolean isAdmin = isAdminUser(username);

        // Créer les claims pour l'utilisateur
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(username)
                .claim("roles", scope);

        // Marquer l'utilisateur comme administrateur si nécessaire
        if (isAdmin) {
            claimsBuilder.claim("isAdmin", true);
        }

        JwtClaimsSet claims = claimsBuilder.build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    // Méthode pour vérifier si l'utilisateur est l'administrateur prédéfini
    private boolean isAdminUser(String username) {
        return username.equals("admin@example.com"); // Vérifiez si l'utilisateur est l'administrateur par son email
    }
}
