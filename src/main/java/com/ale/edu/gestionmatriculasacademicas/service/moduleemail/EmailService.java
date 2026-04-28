package com.ale.edu.gestionmatriculasacademicas.service.moduleemail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void sendPasswordRecovery(String emailDestino, String token) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(emailDestino);
            helper.setSubject("Recuperación de contraseña");
            helper.setText(construirHtml(token), true); // true = es HTML

            mailSender.send(mensaje);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando email", e);
        }
    }

    private String construirHtml(String token) {
        String link = frontendUrl + "/reset-password?token=" + token;

        return """
            <html>
              <body style="font-family: Arial, sans-serif;">
                <h2>Recuperación de contraseña</h2>
                <p>Haz clic en el botón para restablecer tu contraseña.
                   Este link expira en <strong>5 minutos</strong>.</p>
                <a href="%s"
                   style="background:#1976D2; color:white; padding:12px 24px;
                          text-decoration:none; border-radius:4px;">
                  Restablecer contraseña
                </a>
                <p>Si no solicitaste esto, ignora este mensaje.</p>
              </body>
            </html>
        """.formatted(link);
    }
}