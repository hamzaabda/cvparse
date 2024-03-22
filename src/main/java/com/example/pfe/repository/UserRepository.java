package com.example.pfe.repository;

import com.example.pfe.models.ApplicationUser;
import com.example.pfe.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {
    Optional<ApplicationUser> findByUsername(String username);

    Optional<ApplicationUser> findByEmail(String email);
    boolean existsByEmail(String email); // Ajout de la méthode existsByEmail


    // Définir une méthode pour compter les connexions réussies pour une journée spécifique
    @Query("SELECT COUNT(u) FROM ApplicationUser u WHERE DATE(u.lastLoginDate) = DATE(:date)")
    long countSuccessfulLoginsByDay(@Param("date") Date date);

    // Définir une méthode pour compter les réinitialisations de mot de passe pour un mois spécifique
    @Query("SELECT COUNT(u) FROM ApplicationUser u WHERE YEAR(u.passwordResetDate) = :year AND MONTH(u.passwordResetDate) = :month")
    long countPasswordResetsByMonth(@Param("year") int year, @Param("month") int month);

    // Définir une méthode pour compter les utilisateurs inscrits pour un mois spécifique
    @Query("SELECT COUNT(u) FROM ApplicationUser u WHERE YEAR(u.registrationDate) = :year AND MONTH(u.registrationDate) = :month")
    long countRegisteredUsersByMonth(@Param("year") int year, @Param("month") int month);
}




