package com.ito.hva_act4_t4.dto;

public class AuthResponse {
    private String token;
    private String tipo = "Bearer";
    private String username;

    public AuthResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() { return token; }
    public String getTipo() { return tipo; }
    public String getUsername() { return username; }
}