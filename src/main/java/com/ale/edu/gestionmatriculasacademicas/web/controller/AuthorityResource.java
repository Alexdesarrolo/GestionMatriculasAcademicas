package com.ale.edu.gestionmatriculasacademicas.web.controller;

import com.ale.edu.gestionmatriculasacademicas.domain.Authority;
import com.ale.edu.gestionmatriculasacademicas.repository.AuthorityRepository;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.BadRequestException;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.ResourceNotFoundException;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authorities")
@Transactional
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AuthorityResource {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorityResource.class);

    private final AuthorityRepository authorityRepository;

    public AuthorityResource(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    // POST /api/authorities
    @PostMapping
    public ResponseEntity<Authority> createAuthority(@Valid @RequestBody Authority authority) throws URISyntaxException {
        LOG.debug("REST request to save Authority : {}", authority);
        if (authorityRepository.existsById(authority.getName())) {
            throw new BadRequestException("El rol ya existe: " + authority.getName());
        }
        Authority result = authorityRepository.save(authority);
        return ResponseEntity
            .created(new URI("/api/authorities/" + result.getName()))
            .body(result);
    }

    // GET /api/authorities
    @GetMapping
    public List<Authority> getAllAuthorities() {
        LOG.debug("REST request to get all Authorities");
        return authorityRepository.findAll();
    }

    // GET /api/authorities/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Authority> getAuthority(@PathVariable String id) {
        LOG.debug("REST request to get Authority : {}", id);
        return authorityRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/authorities/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthority(@PathVariable String id) {
        LOG.debug("REST request to delete Authority : {}", id);
        if (!authorityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rol no encontrado: " + id);
        }
        authorityRepository.deleteById(id);
    }
}