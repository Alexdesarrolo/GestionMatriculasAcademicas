package com.ale.edu.gestionmatriculasacademicas.service.dto;

import jakarta.validation.constraints.Size;

/**
 * A DTO representing a password change with a key.
 */
public class KeyAndPasswordDTO {

    private String key;

    @Size(min = 8, max = 15)
    private String newPassword;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
