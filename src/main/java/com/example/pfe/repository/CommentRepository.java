package com.example.pfe.repository;

import com.example.pfe.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Ajouter cette méthode pour récupérer les commentaires par post
    List<Comment> findByPostId(Long postId);
}
