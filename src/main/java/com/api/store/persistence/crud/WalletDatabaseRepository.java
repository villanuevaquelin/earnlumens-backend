package com.api.store.persistence.crud;

import com.api.store.persistence.dbo.LogDBO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WalletDatabaseRepository extends MongoRepository<LogDBO, String> {
    Optional<LogDBO> findByAlog(String accessToken);
}
