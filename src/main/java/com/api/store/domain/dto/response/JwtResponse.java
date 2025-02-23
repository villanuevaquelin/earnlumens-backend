package com.api.store.domain.dto.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private List<String> roles;
    private String xlmAddress;

    public JwtResponse(String accessToken, String id, String username, String xlmAddress, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.xlmAddress = xlmAddress;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getXlmAddress() {
        return xlmAddress;
    }

    public void setXlmAddress(String xlmAddress) {
        this.xlmAddress = xlmAddress;
    }
}
