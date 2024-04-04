package com.example.pfe.repository;

import com.example.pfe.models.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    // Vous pouvez ajouter des méthodes spécifiques si nécessaire
    List<Recruiter> findByEmailContaining(String email);
    Optional<Recruiter> findByEmail(String email);
}
