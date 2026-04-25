package com.ale.edu.gestionmatriculasacademicas.service;

import com.ale.edu.gestionmatriculasacademicas.domain.User;
import com.ale.edu.gestionmatriculasacademicas.service.dto.PasswordResetTokenDTO;

import java.util.Optional;

public interface PasswordResetTokenService {
    String createToken(User user);
    Optional<User> validateToken(String token);
    void completePasswordReset(String newPassword, User user, String token);
}
