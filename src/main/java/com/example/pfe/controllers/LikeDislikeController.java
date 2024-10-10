package com.example.pfe.controllers;

import com.example.pfe.models.LikeDislike;
import com.example.pfe.services.LikeDislikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/likes")
public class LikeDislikeController {

    @Autowired
    private LikeDislikeService likeDislikeService;

    @PostMapping
    public LikeDislike likeDislikePost(@RequestBody LikeDislike likeDislike) {
        return likeDislikeService.addLikeDislike(likeDislike);
    }

    @GetMapping("/post/{postId}/likes")
    public long countLikes(@PathVariable Long postId) {
        return likeDislikeService.countLikesByPostId(postId);
    }

    @GetMapping("/post/{postId}/dislikes")
    public long countDislikes(@PathVariable Long postId) {
        return likeDislikeService.countDislikesByPostId(postId);
    }
}
