package com.api.store.domain.repository;

import com.api.store.domain.TempUser;

import java.util.Optional;

public interface TempUserRepository {

    Optional<TempUser> findByUsername(String username);

    Boolean existsByUsername(String username);

    TempUser save(TempUser tempUser);

    void deleteById(String id);
}
