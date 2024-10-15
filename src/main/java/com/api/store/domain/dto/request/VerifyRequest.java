package com.api.store.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public class VerifyRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String codex;

    private String captchaResponse;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCodex() {
        return codex;
    }

    public void setCodex(String codex) {
        this.codex = codex;
    }

    public String getCaptchaResponse() {
        return captchaResponse;
    }

    public void setCaptchaResponse(String captchaResponse) {
        this.captchaResponse = captchaResponse;
    }
}
