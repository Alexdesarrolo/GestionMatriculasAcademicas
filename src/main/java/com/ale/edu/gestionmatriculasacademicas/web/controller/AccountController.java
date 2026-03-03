package com.ale.edu.gestionmatriculasacademicas.web.controller;

import com.ale.edu.gestionmatriculasacademicas.service.AccountService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.LoginDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.PasswordChangeDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.TokenDTO;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        String token = accountService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return ResponseEntity.ok(new TokenDTO(token));
    }

    @PostMapping("/account/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeDTO dto) {
        accountService.changePassword(dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
}