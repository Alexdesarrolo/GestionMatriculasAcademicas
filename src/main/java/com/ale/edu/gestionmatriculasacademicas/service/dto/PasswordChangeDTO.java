package com.ale.edu.gestionmatriculasacademicas.service.dto;


/**
 * A DTO representing a password change required data - current and new password.
 */
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordChangeDTO {

    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min = 8, max = 15)
    private String newPassword;

    public PasswordChangeDTO() {}

    public PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
