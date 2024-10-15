package com.api.store.persistence.crud;

import com.api.store.persistence.dbo.TempUserDBO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TempUserDatabaseRepository extends MongoRepository<TempUserDBO, String> {

    Optional<TempUserDBO> findByUsername(String username);

    Boolean existsByUsername(String username);
}
