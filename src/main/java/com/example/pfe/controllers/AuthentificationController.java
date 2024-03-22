package com.example.pfe.controllers;


import com.example.pfe.models.ApplicationUser;
import com.example.pfe.models.LoginResponseDTO;
import com.example.pfe.models.RegistrationDTO;
import com.example.pfe.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthentificationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationDTO body){
        try {
            ApplicationUser user = authenticationService.registerUser(body.getUsername(), body.getPassword(), body.getEmail());
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody RegistrationDTO body) {
        LoginResponseDTO responseDTO = authenticationService.loginUser(body.getEmail(), body.getPassword());
        // Retirez l'appel à saveSuccessfulLogin
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        // Appel du service de déconnexion
        authenticationService.logout();

        // Réponse réussie avec un objet JSON contenant un message
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email) {
        try {
            authenticationService.resetPassword(email);
            // Retirez l'appel à savePasswordReset
            return ResponseEntity.ok("Password reset successful. Check your email for the new password.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users/count")
    public ResponseEntity<Long> getTotalUsersCount() {
        long totalUsersCount = authenticationService.getTotalUsersCount();
        return ResponseEntity.ok(totalUsersCount);
    }

    @GetMapping("/logins/count")
    public ResponseEntity<Long> getSuccessfulLoginsCountByDay(@RequestParam Date date) {
        long loginsCount = authenticationService.getSuccessfulLoginsCountByDay(date);
        return ResponseEntity.ok(loginsCount);
    }

    @GetMapping("/password-resets/count")
    public ResponseEntity<Long> getPasswordResetsCountByMonth(@RequestParam int year, @RequestParam int month) {
        long resetsCount = authenticationService.getPasswordResetsCountByMonth(year, month);
        return ResponseEntity.ok(resetsCount);
    }

    @GetMapping("/registrations/count")
    public ResponseEntity<Long> getRegisteredUsersCountByMonth(@RequestParam int year, @RequestParam int month) {
        long registrationsCount = authenticationService.getRegisteredUsersCountByMonth(year, month);
        return ResponseEntity.ok(registrationsCount);
    }

    @PostMapping("/admin/add")
    public ResponseEntity<?> addAdmin(@Valid @RequestBody RegistrationDTO body) {
        try {
            authenticationService.addAdmin(body.getUsername(), body.getPassword(), body.getEmail());
            return ResponseEntity.ok(Map.of("message", "Admin added successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @PutMapping("/admin/update/{adminId}")
    public ResponseEntity<?> updateAdmin(@PathVariable long adminId, @Valid @RequestBody RegistrationDTO body) {
        try {
            authenticationService.updateAdmin(adminId, body.getUsername(), body.getPassword(), body.getEmail());
            return ResponseEntity.ok(Map.of("message", "Admin updated successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/admin/delete/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable long adminId) {
        try {
            authenticationService.deleteAdmin(adminId);
            return ResponseEntity.ok(Map.of("message", "Admin deleted successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<ApplicationUser> users = authenticationService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


}
