package com.example.pfe.models;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(unique = true) // Ajout pour email unique
    private String email;

    private boolean enabled = true; // Ajout d'un champ pour le statut activé/désactivé

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate; // Ajout du champ lastLoginDate

    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordResetDate; // Ajout du champ passwordResetDate

    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate; // Ajout du champ registrationDate




    // Getters and setters

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_junction", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<Role> authorities;

    public ApplicationUser() {
        super();
        authorities = new HashSet<>();
    }

    public ApplicationUser(Integer userId, String username, String password, Set<Role> authorities, String email) {
        super();
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.email = email; // Initialisation de l'email

    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /* If you want account locking capabilities create variables and ways to set them for the methods below */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



}

