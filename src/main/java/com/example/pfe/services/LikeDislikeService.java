package com.example.pfe.services;

import com.example.pfe.models.LikeDislike;
import com.example.pfe.repository.LikeDislikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeDislikeService {

    @Autowired
    private LikeDislikeRepository likeDislikeRepository;

    public LikeDislike addLikeDislike(LikeDislike likeDislike) {
        return likeDislikeRepository.save(likeDislike);
    }

    public long countLikesByPostId(Long postId) {
        return likeDislikeRepository.countByPostIdAndLikedTrue(postId);
    }

    public long countDislikesByPostId(Long postId) {
        return likeDislikeRepository.countByPostIdAndLikedFalse(postId);
    }
}
