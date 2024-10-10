package com.example.pfe.controllers;

import com.example.pfe.models.Profile;
import com.example.pfe.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/upload")
    public ResponseEntity<Profile> uploadCV(@RequestParam("file") MultipartFile file) {
        try {
            Profile profile = profileService.createProfileFromCV(file); // Récupérez le profil créé
            return ResponseEntity.status(HttpStatus.CREATED).body(profile); // Retournez le profil
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Gérer l'erreur
        }
    }
}
