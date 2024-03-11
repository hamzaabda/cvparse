package com.example.pfe.models;

public class LoginResponseDTO {
    private ApplicationUser user;
    private String jwt;
    private long totalUsersCount; // Ajoutez cette ligne

    public LoginResponseDTO(){
        super();
    }

    public LoginResponseDTO(ApplicationUser user, String jwt, long totalUsersCount){
        this.user = user;
        this.jwt = jwt;
        this.totalUsersCount = totalUsersCount;
    }


    public ApplicationUser getUser(){
        return this.user;
    }

    public void setUser(ApplicationUser user){
        this.user = user;
    }

    public String getJwt(){
        return this.jwt;
    }

    public void setJwt(String jwt){
        this.jwt = jwt;
    }

    public long getTotalUsersCount() { // Ajoutez cette méthode
        return totalUsersCount;
    }

    public void setTotalUsersCount(long totalUsersCount) { // Ajoutez cette méthode
        this.totalUsersCount = totalUsersCount;
    }
}
