package com.example.pfe.controllers;


import com.example.pfe.models.BlogPost;
import com.example.pfe.services.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/blogposts")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @GetMapping
    public List<BlogPost> getAllBlogPosts() {
        return blogPostService.getAllBlogPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getBlogPostById(@PathVariable Long id) {
        return blogPostService.getBlogPostById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public BlogPost createBlogPost(@RequestBody BlogPost blogPost) {
        return blogPostService.createOrUpdateBlogPost(blogPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updateBlogPost(@PathVariable Long id, @RequestBody BlogPost blogPost) {
        return blogPostService.getBlogPostById(id)
                .map(existingBlogPost -> {
                    existingBlogPost.setTitle(blogPost.getTitle());
                    existingBlogPost.setContent(blogPost.getContent());
                    existingBlogPost.setUpdatedAt(blogPost.getUpdatedAt());
                    return ResponseEntity.ok(blogPostService.createOrUpdateBlogPost(existingBlogPost));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable Long id) {
        return blogPostService.getBlogPostById(id)
                .map(blogPost -> {
                    blogPostService.deleteBlogPost(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
