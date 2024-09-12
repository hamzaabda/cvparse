package com.example.pfe.services;

import com.example.pfe.models.Document;
import com.example.pfe.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

    // Nouvelle méthode pour récupérer tous les documents
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }
}
