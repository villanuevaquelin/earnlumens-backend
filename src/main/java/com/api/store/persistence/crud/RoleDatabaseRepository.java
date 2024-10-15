package com.api.store.persistence.crud;

import com.api.store.persistence.dbo.ERoleDBO;
import com.api.store.persistence.dbo.RoleDBO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;



public interface RoleDatabaseRepository extends MongoRepository<RoleDBO, String> {
    Optional<RoleDBO> findByName(ERoleDBO name);
}
