package com.api.store.persistence.dbo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class RoleDBO {
    @Id
    private String id;

    private ERoleDBO name;

    public RoleDBO() {

    }

    public RoleDBO(ERoleDBO name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ERoleDBO getName() {
        return name;
    }

    public void setName(ERoleDBO name) {
        this.name = name;
    }
}
