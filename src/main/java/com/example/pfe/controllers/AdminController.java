package com.example.pfe.controllers;

import com.example.pfe.models.Recruiter;
import com.example.pfe.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/recruiters")

    public ResponseEntity<String> addRecruiter(@RequestBody Recruiter recruiter) {
        adminService.addRecruiter(recruiter);
        return ResponseEntity.status(HttpStatus.CREATED).body("Recruiter added successfully");
    }

    @PutMapping("/recruiters/{recruiterId}")
    public ResponseEntity<String> updateRecruiter(@PathVariable Long recruiterId, @RequestBody Recruiter recruiterDetails) {
        adminService.updateRecruiter(recruiterId, recruiterDetails);
        return ResponseEntity.ok("Recruiter updated successfully");
    }

    @DeleteMapping("/recruiters/{recruiterId}")

    public ResponseEntity<String> deleteRecruiter(@PathVariable Long recruiterId) {
        try {
            adminService.deleteRecruiter(recruiterId);
            return ResponseEntity.ok("Recruiter deleted successfully");
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete recruiter: " + e.getMessage());
        }
    }
    // Endpoint pour récupérer tous les recruteurs
    @GetMapping("/recruiters")
    public List<Recruiter> getAllRecruiters() {
        return adminService.getAllRecruiters();
    }
    @GetMapping("/recruiters/search")
    public List<Recruiter> searchRecruitersByEmail(@RequestParam String email) {
        return adminService.searchRecruitersByEmail(email);
    }
}
