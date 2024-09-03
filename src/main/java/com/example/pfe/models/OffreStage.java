package com.example.pfe.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "OffreStage")
public class OffreStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String competencesRequises;
    private String localisation;
    private String niveauEducationRequis;
    private String typeStage; // Renommé de typeContrat
    private String dateDebut;

    private LocalDate dateExpiration;

    @OneToMany(mappedBy = "offreStage")
    private List<Feedback> feedbacks;

    // Getters and setters
    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    @ElementCollection
    private List<String> cvs; // Utilisation d'une liste pour stocker les noms de fichiers des CVs

    // Getters and setters pour id et autres propriétés

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }


    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompetencesRequises() {
        return competencesRequises;
    }

    public void setCompetencesRequises(String competencesRequises) {
        this.competencesRequises = competencesRequises;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getNiveauEducationRequis() {
        return niveauEducationRequis;
    }

    public void setNiveauEducationRequis(String niveauEducationRequis) {
        this.niveauEducationRequis = niveauEducationRequis;
    }

    public String getTypeStage() {
        return typeStage;
    }

    public void setTypeStage(String typeStage) {
        this.typeStage = typeStage;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public List<String> getCvs() {
        return cvs;
    }

    public void setCvs(List<String> cvs) {
        this.cvs = cvs;
    }

}
