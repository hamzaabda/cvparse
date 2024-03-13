package com.example.pfe.repository;

import com.example.pfe.models.ApplicationUser;
import com.example.pfe.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {
    Optional<ApplicationUser> findByUsername(String username);

    Optional<ApplicationUser> findByEmail(String email);
    boolean existsByEmail(String email); // Ajout de la méthode existsByEmail

}

