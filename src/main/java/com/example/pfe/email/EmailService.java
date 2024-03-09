package com.example.pfe.email;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Service
public class EmailService {

    private final String host = "smtp.gmail.com";
    private final String port = "587";
    private final String username = "hamzaabda09@gmail.com"; // Mettez votre adresse e-mail ici
    private final String password = "hujh nrvz ngse qsvn"; // Mettez votre mot de passe ici

    private Session session; // Déclarez la variable session ici

    public EmailService() {
        // Paramètres pour la session de messagerie
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Créer une session de messagerie avec authentification
        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendConfirmationEmail(String recipientEmail) throws MessagingException {
        // Créer un message e-mail
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Confirmation d'inscription");
        message.setText("Bonjour,\n\nVotre inscription a été confirmée avec succès.\n\nCordialement,\nVotre application");

        // Envoyer le message
        Transport.send(message);

        System.out.println("E-mail de confirmation envoyé avec succès à " + recipientEmail);
    }

    public void sendResetPasswordEmail(String recipientEmail, String newPassword) throws MessagingException {
        // Créer un message e-mail pour réinitialiser le mot de passe
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Réinitialisation de votre mot de passe");
        message.setText("Bonjour,\n\nVotre nouveau mot de passe est : " + newPassword + "\n\nCordialement,\nVotre application");

        // Envoyer le message
        Transport.send(message);

        System.out.println("E-mail de réinitialisation de mot de passe envoyé avec succès à " + recipientEmail);
    }
}
