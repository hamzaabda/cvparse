package com.example.pfe.models;

public class EntretienDTO {

    private String titre;
    private String startDate;  // Format ISO : "2024-09-15T10:00:00"
    private String endDate;

    // Getter pour titre
    public String getTitre() {
        return titre;
    }

    // Setter pour titre
    public void setTitre(String titre) {
        this.titre = titre;
    }

    // Getter pour startDate
    public String getStartDate() {
        return startDate;
    }

    // Setter pour startDate
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    // Getter pour endDate
    public String getEndDate() {
        return endDate;
    }

    // Setter pour endDate
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "EntretienDTO{" +
                "titre='" + titre + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
