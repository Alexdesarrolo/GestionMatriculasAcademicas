package com.ale.edu.gestionmatriculasacademicas.service.moduleemail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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
            helper.setText(construirHtml(token), true);

            mailSender.send(mensaje);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando email", e);
        }
    }

    private String construirHtml(String token) {
        String link = frontendUrl + "/reset-password?token=" + token;
        String contenido = """
            <h2 style="color: #0F172A; margin-top: 0;">Recuperación de contraseña</h2>
            <p style="color: #475569; line-height: 1.6;">Haz clic en el botón para restablecer tu contraseña.
               Este link expira en <strong>5 minutos</strong>.</p>
            <div style="margin: 30px 0;">
                <a href="%s"
                   style="background:#2563EB; color:white; padding:12px 24px;
                          text-decoration:none; border-radius:4px; font-weight: 600;
                          box-shadow: 0 4px 6px -1px rgba(37, 99, 235, 0.1);">
                  Restablecer contraseña
                </a>
            </div>
            <p style="color: #64748B; font-size: 0.875rem;">Si no solicitaste esto, puedes ignorar este mensaje con seguridad.</p>
        """.formatted(link);
        
        return wrapHtml(contenido);
    }

    public void sendCredentials(String emailDestino, String password, String login) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(emailDestino);
            helper.setSubject("Credenciales de acceso");
            helper.setText(construirHtmlCredentials(password, login), true);

            mailSender.send(mensaje);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando email", e);
        }
    }

    private String construirHtmlCredentials(String password, String login) {
        String contenido = """
            <h2 style="color: #0F172A; margin-top: 0;">¡Bienvenido a la plataforma!</h2>
            <p style="color: #475569; line-height: 1.6;">Tu cuenta ha sido creada exitosamente. Aquí están tus credenciales de acceso:</p>
            <div style="background: #F8FAFC; border: 1px solid #E2E8F0; border-radius: 8px; padding: 20px; margin: 24px 0;">
                <p style="margin: 0 0 10px 0; color: #475569;"><strong>Usuario:</strong> <code style="background: #fff; padding: 2px 6px; border-radius: 4px;">%s</code></p>
                <p style="margin: 0; color: #475569;"><strong>Contraseña:</strong> <code style="background: #fff; padding: 2px 6px; border-radius: 4px;">%s</code></p>
            </div>
            <p style="color: #475569; line-height: 1.6;">Te recomendamos cambiar tu contraseña después de iniciar sesión por primera vez.</p>
            <div style="margin: 30px 0;">
                <a href="%s/login"
                   style="background:#2563EB; color:white; padding:12px 24px;
                          text-decoration:none; border-radius:4px; font-weight: 600;
                          box-shadow: 0 4px 6px -1px rgba(37, 99, 235, 0.1);">
                  Iniciar sesión
                </a>
            </div>
        """.formatted(login, password, frontendUrl);
        
        return wrapHtml(contenido);
    }

    private String wrapHtml(String contenido) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #F8FAFC; }
                    .container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 8px; border: 1px solid #E2E8F0; overflow: hidden; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); }
                    .header { text-align: center; padding: 32px 20px; border-bottom: 1px solid #F1F5F9; }
                    .content { padding: 40px; }
                    .footer { padding: 24px; background: #F8FAFC; text-align: center; font-size: 12px; color: #94A3B8; border-top: 1px solid #F1F5F9; }
                    .logo-container { display: inline-block; background-color: #2563EB; padding: 6px; border-radius: 4px; }
                    .logo-svg { width: 20px; height: 20px; display: block; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <table align="center" border="0" cellpadding="0" cellspacing="0" style="margin: 0 auto;">
                            <tr>
                                <td style="background-color: #2563EB; padding: 4px; border-radius: 8px; width: 32px; height: 32px; text-align: center; vertical-align: middle;">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="20" height="20" style="display: block;">
                                        <path d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>
                                    </svg>
                                </td>
                                <td style="padding-left: 10px;">
                                    <span style="font-size: 22px; font-weight: bold; color: #0F172A; font-family: monospace;">Matricula<span style="color: #2563EB;">+</span></span>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="content">
        """ + contenido + """
                    </div>
                    <div class="footer">
                        &copy; 2026 Matricula+. Sistema de Gestión Académica.<br>
                        Este es un mensaje automático, por favor no lo respondas.
                    </div>
                </div>
            </body>
            </html>
        """;
    }

}