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
        String emailContent = buildEmail("Username", "Message", "Link"); // Appel à la méthode buildEmail avec les paramètres appropriés
        message.setContent(emailContent, "text/html"); // Spécifiez que le contenu est HTML

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
        String emailContent = buildEmailWithLogo("Username", "Message", "Link"); // Appel à la méthode buildEmailWithLogo avec les paramètres appropriés
        message.setContent(emailContent, "text/html"); // Spécifiez que le contenu est HTML

        // Envoyer le message
        Transport.send(message);

        System.out.println("E-mail de réinitialisation de mot de passe envoyé avec succès à " + recipientEmail);
    }

    public String buildEmail(String username, String message, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "            <tbody>\n" +
                "              <tr>\n" +
                "                <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                  <a href=\"#\">\n" +
                "                    <img src=\"\" alt=\"Logo\" width=\"50\" height=\"50\" style=\"display:block;border:0;outline:none;text-decoration:none\" height=\"44\">\n" +
                "                  </a>\n" +
                "                </td>\n" +
                "                <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                  <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "        <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "          <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + username + ",</p>\n" +
                "          <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + message + "</p>\n" +
                "          <blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">\n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n" +
                "              <a href=\"" + link + "\">Activate Now</a>\n" +
                "            </p>\n" +
                "          </blockquote>\n" +
                "          " +
                "          <p>See you soon</p>\n" +
                "        </td>\n" +
                "        <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "</div>";
    }

    public String buildEmailWithLogo(String username, String message, String link) {
        String logoImageUrl = "https://i.imgur.com/UWWhZX9.png";

        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "            <tbody>\n" +
                "              <tr>\n" +
                "                <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                  <a href=\"#\">\n" +
                "                    <img src=\"" + logoImageUrl + "\" alt=\"Logo\" width=\"50\" height=\"50\" style=\"display:block;border:0;outline:none;text-decoration:none\" height=\"44\">\n" +
                "                  </a>\n" +
                "                </td>\n" +
                "                <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                  <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirmez votre adresse e-mail</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "        <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "          <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Bonjour " + username + ",</p>\n" +
                "          <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + message + "</p>\n" +
                "          <blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">\n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n" +
                "              <a href=\"" + link + "\">Cliquez ici pour activer votre compte</a>\n" +
                "            </p>\n" +
                "          </blockquote>\n" +
                "          Ce lien expirera dans 15 minutes.\n" +
                "          <p>A bientôt!</p>\n" +
                "        </td>\n" +
                "        <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "</div>";
    }
}
