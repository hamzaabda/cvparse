package com.example.pfe.repository;

import com.example.pfe.models.LikeDislike;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {

    // Méthode pour compter les likes
    long countByPostIdAndLikedTrue(Long postId);

    // Méthode pour compter les dislikes
    long countByPostIdAndLikedFalse(Long postId);
}
