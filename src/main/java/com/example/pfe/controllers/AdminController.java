package com.example.pfe.controllers;

import com.example.pfe.models.Recruiter;
import com.example.pfe.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/recruiters")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addRecruiter(@RequestBody Recruiter recruiter) {
        adminService.addRecruiter(recruiter);
        return ResponseEntity.status(HttpStatus.CREATED).body("Recruiter added successfully");
    }

    @PutMapping("/recruiters/{recruiterId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateRecruiter(@PathVariable Long recruiterId, @RequestBody Recruiter recruiterDetails) {
        adminService.updateRecruiter(recruiterId, recruiterDetails);
        return ResponseEntity.ok("Recruiter updated successfully");
    }

    @DeleteMapping("/recruiters/{recruiterId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteRecruiter(@PathVariable Long recruiterId) {
        adminService.deleteRecruiter(recruiterId);
        return ResponseEntity.ok("Recruiter deleted successfully");
    }
    
}
