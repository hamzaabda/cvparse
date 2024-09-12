package com.example.pfe.controllers;

import com.example.pfe.models.Document;
import com.example.pfe.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/documents")
    public ResponseEntity<Document> uploadDocument(@RequestParam("nom") String nom,
                                                   @RequestParam("type") String type,
                                                   @RequestParam("contenu") MultipartFile contenu) {
        Document document = new Document();
        document.setNom(nom);
        document.setType(type);
        try {
            document.setContenu(contenu.getBytes()); // Convert MultipartFile to byte[]
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Document savedDocument = documentService.saveDocument(document);
        return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
    }

    // Téléchargement d'un fichier spécifique par ID
    @GetMapping("/documents/{id}")
    public ResponseEntity<byte[]> getDocument(@PathVariable Long id) {
        Optional<Document> document = documentService.getDocumentById(id);
        if (document.isPresent()) {
            Document doc = document.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(doc.getType()));
            headers.setContentDispositionFormData("attachment", doc.getNom());
            return new ResponseEntity<>(doc.getContenu(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupération de la liste de tous les documents
    @GetMapping("/documents")
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
