package com.ale.edu.gestionmatriculasacademicas.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ale.edu.gestionmatriculasacademicas.service.AccountService;
import com.ale.edu.gestionmatriculasacademicas.domain.User;
import com.ale.edu.gestionmatriculasacademicas.service.PasswordResetTokenService;
import com.ale.edu.gestionmatriculasacademicas.service.UserService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AdminUserDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.KeyAndPasswordDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.LoginDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.PasswordChangeDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.TokenDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;

    public AccountController(AccountService accountService, PasswordEncoder passwordEncoder, UserService userService, PasswordResetTokenService passwordResetTokenService) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        String token = accountService.login(loginDTO.getUsername(), loginDTO.getPassword());
        boolean mustChangePassword = userService.getUserWithAuthoritiesByLogin(loginDTO.getUsername())
            .map(User::isMustChangePassword)
            .orElse(false);
        return ResponseEntity.ok(new TokenDTO(token, mustChangePassword));
    }

    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService.getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        userService.updateUserAccount(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    @PostMapping("/account/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeDTO dto) {
        accountService.changePassword(dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/account/reset-password/init")
    public ResponseEntity<String> requestPasswordReset(@RequestBody String mail) {
        return userService.findOneByEmailIgnoreCase(mail)
            .map(user -> {
                String token = passwordResetTokenService.createTokenAndSendEmail(user);
                // In a real app, send mail here. For now, return token.
                return ResponseEntity.ok(token);
            })
            .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/account/reset-password/finish")
    public ResponseEntity<Object> finishPasswordReset(@Valid @RequestBody KeyAndPasswordDTO keyAndPassword) {
        try {
            return passwordResetTokenService.validateToken(keyAndPassword.getKey())
                .map(user -> {
                    passwordResetTokenService.completePasswordReset(keyAndPassword.getNewPassword(), user, keyAndPassword.getKey());
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.badRequest().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


@GetMapping("/test-hash")
public String testHash() {
    return passwordEncoder.encode("admin");
}
}