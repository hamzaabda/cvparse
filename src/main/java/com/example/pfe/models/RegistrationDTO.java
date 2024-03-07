package com.example.pfe.models;

public class RegistrationDTO {
    private String username;
    private String password;
    private String email; // nouvel attribut email

    public RegistrationDTO(){
        super();
    }

    public RegistrationDTO(String username, String password, String email){ // mise à jour du constructeur
        super();
        this.username = username;
        this.password = password;
        this.email = email; // initialisation de l'attribut email
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail(){ // getter pour l'attribut email
        return this.email;
    }

    public void setEmail(String email){ // setter pour l'attribut email
        this.email = email;
    }

    public String toString(){
        return "Registration info: username: " + this.username + " password: " + this.password + " email: " + this.email; // mise à jour de la méthode toString
    }
}

