package com.ale.edu.gestionmatriculasacademicas.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
public final class SecurityUtils {

    // Clase utilitaria, no se instancia
    private SecurityUtils() {}

    // Obtiene el login del usuario actualmente autenticado
    public static Optional<String> getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return Optional.of(((UserDetails) principal).getUsername());
        }

        if (principal instanceof String) {
            return Optional.of((String) principal);
        }

        return Optional.empty();
    }

    // Verifica si el usuario está autenticado
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
