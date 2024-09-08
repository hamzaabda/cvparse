package com.example.pfe.models;

public class Entretien {

    private String email;
    private String etat; // en attente, accepté, refusé

    // Getters and Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}