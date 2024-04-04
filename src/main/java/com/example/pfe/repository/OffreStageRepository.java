package com.example.pfe.repository;

import com.example.pfe.models.OffreStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreStageRepository extends JpaRepository<OffreStage, Long> {
    // Vous pouvez ajouter des méthodes personnalisées ici si nécessaire
    List<OffreStage> findByTypeStageAndNiveauEducationRequis(String typeStage, String niveauEducationRequis);

}
