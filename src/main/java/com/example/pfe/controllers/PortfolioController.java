package com.example.pfe.controllers;

import com.example.pfe.models.PortfolioData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth")
public class PortfolioController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/generate")
    public ResponseEntity<String> generatePortfolio(@RequestBody PortfolioData portfolioData) {
        String nodeJsUrl = "http://localhost:3001/generate-portfolio";
        try {
            String response = restTemplate.postForObject(nodeJsUrl, portfolioData, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating portfolio");
        }
    }

}