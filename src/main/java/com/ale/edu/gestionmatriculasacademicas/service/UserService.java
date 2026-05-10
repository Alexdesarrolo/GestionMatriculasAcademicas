package com.ale.edu.gestionmatriculasacademicas.service;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ale.edu.gestionmatriculasacademicas.domain.Authority;
import com.ale.edu.gestionmatriculasacademicas.domain.User;
import com.ale.edu.gestionmatriculasacademicas.repository.AuthorityRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.UserRepository;
import com.ale.edu.gestionmatriculasacademicas.security.SecurityUtils;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AdminUserDTO;
import com.ale.edu.gestionmatriculasacademicas.service.moduleemail.EmailService;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.InvalidPasswordException;

@Service
@Transactional
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final EmailService emailService;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.emailService = emailService;
    }

    // Crear usuario desde el panel admin
    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        String password = generarCadenaAleatoria();
        userDTO.setPassword(password);
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail() != null ? userDTO.getEmail().toLowerCase() : null);
        user.setImageUrl(userDTO.getImageUrl());
        user.setLangKey(userDTO.getLangKey() != null ? userDTO.getLangKey() : "es");
        user.setPassword(passwordEncoder.encode(password));
        user.setActivated(true);
        user.setMustChangePassword(true);

         user.setCreatedBy(SecurityUtils.getCurrentUserLogin().orElse("system"));
        user.setLastModifiedBy(SecurityUtils.getCurrentUserLogin().orElse("system"));

        // Asignar roles
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }

        emailService.sendCredentials(user.getEmail(), password, user.getLogin());

        userRepository.save(user);
        LOG.debug("Creado usuario: {}", user.getLogin());
        return user;
    }

    // Actualizar usuario desde el panel admin
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return userRepository.findById(userDTO.getId())
            .map(user -> {
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail() != null ? userDTO.getEmail().toLowerCase() : null);
                user.setImageUrl(userDTO.getImageUrl());
                user.setLangKey(userDTO.getLangKey());
                user.setActivated(userDTO.isActivated());

                if (userDTO.getAuthorities() != null) {
                    Set<Authority> authorities = userDTO.getAuthorities().stream()
                        .map(authorityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet());
                    user.setAuthorities(authorities);
                }

                userRepository.save(user);
                LOG.debug("Actualizado usuario: {}", user.getLogin());
                return new AdminUserDTO(user);
            });
    }

    // Eliminar usuario
    public void deleteUser(String login) {
        userRepository.findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.delete(user);
                LOG.debug("Eliminado usuario: {}", login);
            });
    }

    // Listar todos los usuarios paginados
    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    // Obtener usuario con roles por login
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    // Cambiar contraseña del usuario autenticado
    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                if (!passwordEncoder.matches(currentClearTextPassword, user.getPassword())) {
                    throw new InvalidPasswordException();
                }
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setMustChangePassword(false);
                LOG.debug("Cambió contraseña el usuario: {}", user.getLogin());
            });
    }

    @Transactional(readOnly = true)
    public Optional<User> findOneByEmailIgnoreCase(String email) {
        return userRepository.findOneByEmailIgnoreCase(email);
    }

    public String generarCadenaAleatoria() {
        // 1. Definir los caracteres permitidos
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        
        // 2. Usar StringBuilder para construir la cadena eficientemente
        Random random = new Random();
        int longitud = random.nextInt(5) + 11; // longitud entre 10 y 14 caracteres
        StringBuilder sb = new StringBuilder(longitud);
        

        for (int i = 0; i < longitud; i++) {
            int index = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }

        return sb.toString();
    }
}