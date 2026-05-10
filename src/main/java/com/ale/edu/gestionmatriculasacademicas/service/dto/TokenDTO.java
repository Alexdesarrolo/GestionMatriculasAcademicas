package com.ale.edu.gestionmatriculasacademicas.service.dto;


public class TokenDTO {

    private String token;
    private boolean mustChangePassword;

    public TokenDTO() {}

    public TokenDTO(String token) {
        this.token = token;
    }

    public TokenDTO(String token, boolean mustChangePassword) {
        this.token = token;
        this.mustChangePassword = mustChangePassword;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public boolean isMustChangePassword() { return mustChangePassword; }
    public void setMustChangePassword(boolean mustChangePassword) { this.mustChangePassword = mustChangePassword; }
}
