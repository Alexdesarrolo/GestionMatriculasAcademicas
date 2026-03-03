package com.ale.edu.gestionmatriculasacademicas.service;

import com.ale.edu.gestionmatriculasacademicas.domain.User;
import com.ale.edu.gestionmatriculasacademicas.repository.UserRepository;
import com.ale.edu.gestionmatriculasacademicas.security.SecurityUtils;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.InvalidPasswordException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Cambiar contraseña del usuario autenticado
    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                user.setPassword(passwordEncoder.encode(newPassword));
                LOG.debug("Cambió contraseña el usuario: {}", user.getLogin());
            });
    }

    // Obtener usuario con sus roles (usado internamente por Spring Security)
    @Transactional(readOnly = true)
    public java.util.Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }
}