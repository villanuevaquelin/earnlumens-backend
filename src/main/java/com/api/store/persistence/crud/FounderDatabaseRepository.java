package com.api.store.persistence.crud;

import com.api.store.persistence.dbo.FounderDBO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FounderDatabaseRepository extends MongoRepository<FounderDBO, String> {

    Optional<FounderDBO> findByEmail(String email);
    Boolean existsByEmail(String email);
    long countByEntryDateBetween(LocalDateTime start, LocalDateTime end);
}
