package com.example.pfe.controllers;

import com.example.pfe.email.EmailService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class EmailController {

    @Autowired
    private EmailService emailService;

    private static final String CSV_FILE_PATH = "src/main/resources/emails.csv";

    // Endpoint to retrieve emails that have received an interview invitation
    @GetMapping("/accepted")
    public ResponseEntity<List<String>> getAcceptedCandidateEmails() {
        try {
            List<String> emails = getEmailsFromCSV(CSV_FILE_PATH);
            return new ResponseEntity<>(emails, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Method to read emails from the CSV file
    private List<String> getEmailsFromCSV(String csvFilePath) throws IOException {
        List<String> emails = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(csvFilePath);
             CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream), CSVFormat.DEFAULT.withHeader("Email"))) {
            for (CSVRecord record : csvParser) {
                String email = record.get("Email");
                emails.add(email);
            }
        }
        return emails;
    }

    // Endpoint to save the emails that received an interview invitation
    @GetMapping("/save")
    public ResponseEntity<String> saveAcceptedEmails() {
        try {
            List<String> emails = getEmailsFromCSV(CSV_FILE_PATH);
            saveEmailsToCSV(emails, "src/main/resources/accepted_emails.csv");
            return new ResponseEntity<>("Emails sauvegardés avec succès.", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors de la sauvegarde des emails.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Method to save emails to a CSV file
    private void saveEmailsToCSV(List<String> emails, String csvFilePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(csvFilePath, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (String email : emails) {
                bufferedWriter.write(email);
                bufferedWriter.newLine();
            }
        }
    }

    // Endpoint to accept a candidate
    @PostMapping("/accept/{email}")
    public ResponseEntity<String> acceptCandidate(@PathVariable("email") String email) {
        try {
            // Define the date and time for administrative procedures
            LocalDateTime adminProcedureDateTime = LocalDateTime.now().plusDays(7).withHour(9).withMinute(0);

            // Send acceptance email with administrative procedure details
            emailService.sendAcceptanceEmail(email, adminProcedureDateTime);

            // Update the candidate's status in the CSV file
            updateCandidateStatus(email, "accepté");

            return new ResponseEntity<>("Email d'acceptation envoyé avec succès.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors de l'envoi de l'email d'acceptation.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to reject a candidate
    @PostMapping("/reject/{email}")
    public ResponseEntity<String> rejectCandidate(@PathVariable("email") String email) {
        try {
            // Send rejection email
            emailService.sendRejectionEmail(email);

            // Update the candidate's status in the CSV file
            updateCandidateStatus(email, "refusé");

            return new ResponseEntity<>("Email de refus envoyé avec succès.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors de l'envoi de l'email de refus.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Method to update the candidate's status in the CSV file
    private void updateCandidateStatus(String email, String status) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(CSV_FILE_PATH));
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 1 && parts[0].equals(email)) {
                updatedLines.add(email + "," + status);
            } else {
                updatedLines.add(line);
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE_PATH))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}


