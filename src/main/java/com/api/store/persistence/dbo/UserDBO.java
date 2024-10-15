package com.api.store.persistence.dbo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
public class UserDBO {
    @Id
    private String id;

    @NotBlank
    @Size(max = 50)
    @Email
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Size(max = 120)
    private String timeZone;

    @Size(max = 100)
    private String wpk;

    @DBRef
    private Set<RoleDBO> roles = new HashSet<>();

    public UserDBO() {
    }

    public UserDBO(String username, String password) {
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleDBO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDBO> roles) {
        this.roles = roles;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getWpk() {
        return wpk;
    }

    public void setWpk(String wpk) {
        this.wpk = wpk;
    }
}
