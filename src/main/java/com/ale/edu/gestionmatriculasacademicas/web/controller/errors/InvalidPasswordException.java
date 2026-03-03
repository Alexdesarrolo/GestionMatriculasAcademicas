package com.ale.edu.gestionmatriculasacademicas.web.controller.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Contraseña incorrecta");
    }
}
