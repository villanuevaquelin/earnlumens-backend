package com.api.store.domain;

public class Wallet {

    private String accessToken;
    private String publicKey;
    private String secretKey;

    public Wallet(String accessToken, String publicKey, String secretKey) {
        this.accessToken = accessToken;
        this.publicKey = publicKey;
        this.secretKey = secretKey;
    }

    public Wallet(String secretKey){
        this.secretKey = secretKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
