package com.example.pfe.repository;

import com.example.pfe.models.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    // Vous pouvez ajouter des méthodes spécifiques si nécessaire
}
