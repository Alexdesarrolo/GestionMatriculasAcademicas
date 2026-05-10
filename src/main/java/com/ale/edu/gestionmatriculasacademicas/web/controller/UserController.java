package com.ale.edu.gestionmatriculasacademicas.web.controller;

import com.ale.edu.gestionmatriculasacademicas.domain.User;
import com.ale.edu.gestionmatriculasacademicas.repository.UserRepository;
import com.ale.edu.gestionmatriculasacademicas.service.UserService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AdminUserDTO;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.BadRequestException;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // POST /api/admin/users
    @PostMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AdminUserDTO> createUser(
        @Valid @RequestBody AdminUserDTO userDTO,
        @RequestParam(defaultValue = "true") boolean sendEmail
    ) throws URISyntaxException {
        LOG.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestException("Un nuevo usuario no puede tener ID");
        }
        if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new BadRequestException("El login ya está en uso");
        }
        if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new BadRequestException("El email ya está en uso");
        }

        User newUser = userService.createUser(userDTO, sendEmail);
        AdminUserDTO responseDTO = new AdminUserDTO(newUser);
        if (!sendEmail) {
            responseDTO.setPassword(userDTO.getPassword());
        }

        return ResponseEntity
            .created(new URI("/api/admin/users/" + newUser.getLogin()))
            .body(responseDTO);
    }

    // PUT /api/admin/users/{login}
    @PutMapping("/users/{login}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AdminUserDTO> updateUser(
        @PathVariable String login,
        @Valid @RequestBody AdminUserDTO userDTO
    ) {
        LOG.debug("REST request to update User : {}", userDTO);

        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userDTO.getId())) {
            throw new BadRequestException("El email ya está en uso");
        }

        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userDTO.getId())) {
            throw new BadRequestException("El login ya está en uso");
        }

        Optional<AdminUserDTO> updatedUser = userService.updateUser(userDTO);
        return updatedUser
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/admin/users
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers(Pageable pageable) {
        LOG.debug("REST request to get all Users");
        Page<AdminUserDTO> page = userService.getAllManagedUsers(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    // GET /api/admin/users/{login}
    @GetMapping("/users/{login}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AdminUserDTO> getUser(@PathVariable String login) {
        LOG.debug("REST request to get User : {}", login);
        return userService.getUserWithAuthoritiesByLogin(login)
            .map(AdminUserDTO::new)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/admin/users/{login}
    @DeleteMapping("/users/{login}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String login) {
        LOG.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
    }
}

