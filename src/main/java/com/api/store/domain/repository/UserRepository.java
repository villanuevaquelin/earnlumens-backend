package com.api.store.domain.repository;

import com.api.store.domain.ERole;
import com.api.store.domain.Role;
import com.api.store.domain.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    User save(User user);

    Optional<Role> findByRoleName(ERole name);
}
