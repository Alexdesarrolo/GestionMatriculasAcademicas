package com.ale.edu.gestionmatriculasacademicas.config;

import com.ale.edu.gestionmatriculasacademicas.security.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(
            SecurityUtils.getCurrentUserLogin().orElse("system")
        );
    }
}