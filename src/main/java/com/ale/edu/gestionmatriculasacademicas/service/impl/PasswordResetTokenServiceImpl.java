package com.ale.edu.gestionmatriculasacademicas.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ale.edu.gestionmatriculasacademicas.domain.PasswordResetToken;
import com.ale.edu.gestionmatriculasacademicas.domain.User;
import com.ale.edu.gestionmatriculasacademicas.repository.PasswordResetTokenRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.UserRepository;
import com.ale.edu.gestionmatriculasacademicas.service.PasswordResetTokenService;
import com.ale.edu.gestionmatriculasacademicas.service.moduleemail.EmailService;

@Service
@Transactional
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final Logger log = LoggerFactory.getLogger(PasswordResetTokenServiceImpl.class);

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordResetTokenServiceImpl(
        PasswordResetTokenRepository passwordResetTokenRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        EmailService emailService
    ) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public String createTokenAndSendEmail(User user) {
        log.debug("Request to create password reset token for user: {}", user.getLogin());
        // Invalidate old tokens for this user
        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setTokenHash(token);
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        passwordResetToken.setUsed(false);

        passwordResetTokenRepository.save(passwordResetToken);

        emailService.sendPasswordRecovery(user.getEmail(), token);
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> validateToken(String token) {
        System.out.println("token: ::" + token);
        System.out.println("validando tokenn :: " + passwordEncoder.encode(token));
        log.debug("Request to validate password reset token "+ passwordEncoder.encode(token));
        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenHash(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (resetToken.isExpired() || resetToken.isUsed()) {
            throw new RuntimeException("Token expirado o ya utilizado");
        }

        return Optional.of(resetToken.getUser());

//        return passwordResetTokenRepository.findAllByUsedIsFalseAndExpiresAtAfter(LocalDateTime.now()).stream()
//            .filter(t -> passwordEncoder.matches(token, t.getTokenHash()))
//            .map(PasswordResetToken::getUser)
//            .findFirst().orElse("Token expirado o ya utilizado");
    }

    @Override
    public void completePasswordReset(String newPassword, User user, String token) {
        log.debug("Completing password reset for user: {}", user.getLogin());
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.findAllByUsedIsFalseAndExpiresAtAfter(LocalDateTime.now()).stream()
            .filter(t -> t.getUser().equals(user) && passwordEncoder.matches(token, t.getTokenHash()))
            .findFirst()
            .ifPresent(t -> {
                t.setUsed(true);
                passwordResetTokenRepository.save(t);
            });
    }
}
