package com.example.pfe.models;


import jakarta.persistence.*;

@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255) // Longueur pour le nom
    private String fullName;

    @Column(length = 255) // Longueur pour l'email
    private String email;

    @Column(length = 50) // Longueur pour le numéro de téléphone
    private String phoneNumber;

    @Column(length = 2000) // Longueur augmentée pour l'éducation
    private String education;

    @Column(length = 2000) // Longueur augmentée pour l'expérience
    private String experience;

    @Column(length = 2000) // Longueur augmentée pour les compétences
    private String skills;

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
