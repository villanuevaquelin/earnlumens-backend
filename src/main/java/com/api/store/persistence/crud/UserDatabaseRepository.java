package com.api.store.persistence.crud;

import com.api.store.persistence.dbo.UserDBO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface UserDatabaseRepository extends MongoRepository<UserDBO, String> {
    Optional<UserDBO> findByUsername(String username);

    Boolean existsByUsername(String username);
}
