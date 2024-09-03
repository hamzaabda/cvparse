package com.example.pfe.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Service
public class EmailService {

    private final String host = "smtp.gmail.com";
    private final String port = "587";
    private final String username = "hamzaabda09@gmail.com"; // Your email
    private final String password = "wugv muis tqaj zaar"; // Your password

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final Session session;

    public EmailService() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendConfirmationEmail(String recipientEmail) throws MessagingException {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Confirmation d'inscription");
            message.setText("Votre inscription a été confirmée.");

            Transport.send(message);
            logger.info("E-mail de confirmation envoyé avec succès à " + recipientEmail);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de confirmation : ", e);
            throw e;
        }
    }

    public void sendResetPasswordEmail(String recipientEmail, String newPassword) throws MessagingException {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Réinitialisation de votre mot de passe");

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Votre nouveau mot de passe est : " + newPassword);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            logger.info("E-mail de réinitialisation de mot de passe envoyé avec succès à " + recipientEmail);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de réinitialisation de mot de passe : ", e);
            throw e;
        }
    }

    public void sendAcceptanceOrRejectionEmail(String recipientEmail, boolean isAccepted) throws MessagingException {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            if (isAccepted) {
                // Calculer la date limite pour le partage des documents (4 jours à partir de la date actuelle)
                LocalDate deadlineDate = LocalDate.now().plusDays(4);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedDeadlineDate = deadlineDate.format(formatter);

                message.setSubject("Acceptation de votre candidature au stage");
                message.setText("Félicitations ! Votre candidature au stage a été acceptée.\n\n" +
                        "Veuillez partager vos documents de stage avant le " + formattedDeadlineDate + ".\n\n" +
                        "Merci de votre coopération.");
            } else {
                message.setSubject("Refus de votre candidature au stage");
                message.setText("Nous regrettons de vous informer que votre candidature a été refusée.");
            }

            Transport.send(message);
            logger.info("E-mail de " + (isAccepted ? "acceptation" : "refus") + " envoyé avec succès à " + recipientEmail);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email : ", e);
            throw e;
        }
    }

    public void sendInterviewInvitation(String recipientEmail, LocalDate interviewDate) throws MessagingException {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Invitation à un entretien");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = interviewDate.format(formatter);

            message.setText("Nous vous invitons à un entretien le " + formattedDate + ".\n\n" +
                    "Merci de confirmer votre présence.");

            Transport.send(message);
            logger.info("E-mail d'invitation à l'entretien envoyé avec succès à " + recipientEmail);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email d'invitation à l'entretien : ", e);
            throw e;
        }
    }

    public void sendReclamationResponse(String recipientEmail, String responseMessage) throws MessagingException {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Réponse à votre réclamation");
            message.setText(responseMessage);

            Transport.send(message);
            logger.info("E-mail de réponse à la réclamation envoyé avec succès à " + recipientEmail);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de réponse à la réclamation : ", e);
            throw e;
        }
    }





    public void sendInterviewInvitation(String recipientEmail, LocalDateTime interviewDateTime) throws MessagingException {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Invitation à un entretien");

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedDate = interviewDateTime.format(dateFormatter);
            String formattedTime = interviewDateTime.format(timeFormatter);

            String location = "Siège Social\nImmeuble 3S, Rue Abou Hamed El Ghazeli 1073 Montplaisir – Tunis – Tunisie";

            message.setText("Nous vous invitons à un entretien le " + formattedDate + " à " + formattedTime + ".\n\n" +
                    "Lieu : " + location + "\n\n" +
                    "Merci de confirmer votre présence.");

            Transport.send(message);
            logger.info("E-mail d'invitation à l'entretien envoyé avec succès à " + recipientEmail);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email d'invitation à l'entretien : ", e);
            throw e;
        }
    }




}
