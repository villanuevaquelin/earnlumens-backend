package com.api.store.domain;

import java.util.Date;

public class TempUser {

    private String id;
    private String code;
    private String username;
    private String password;
    private Date lastModifiedDate;
    private String codetry;

    public TempUser(){

    }

    public TempUser(String code, String username, String password) {
        this.code = code;
        this.username = username;
        this.password = password;
        this.lastModifiedDate = new Date();
        this.codetry = "first try";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCodetry() {
        return codetry;
    }

    public void setCodetry(String codetry) {
        this.codetry = codetry;
    }
}
