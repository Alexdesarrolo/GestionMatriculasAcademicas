package com.ale.edu.gestionmatriculasacademicas.service;

import java.util.Optional;

import com.ale.edu.gestionmatriculasacademicas.domain.User;

public interface PasswordResetTokenService {
    String createTokenAndSendEmail(User user);
    Optional<User> validateToken(String token);
    void completePasswordReset(String newPassword, User user, String token);
}
