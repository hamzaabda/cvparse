package com.example.pfe.controllers;


import com.example.pfe.models.ApplicationUser;
import com.example.pfe.models.LoginResponseDTO;
import com.example.pfe.models.RegistrationDTO;
import com.example.pfe.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthentificationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO body){
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
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email) {
        try {
            authenticationService.resetPassword(email);
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
    @PostMapping("/admin/add")
    public ResponseEntity<?> addAdmin(@Valid @RequestBody RegistrationDTO body) {
        try {
            authenticationService.addAdmin(body.getUsername(), body.getPassword(), body.getEmail());
            return ResponseEntity.ok("Admin added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
