package com.api.store.domain.repository;

import com.api.store.domain.Wallet;

import java.util.Optional;

public interface WalletRepository {
    Optional<Wallet> findByAccessToken(String accessToken);
    Wallet save(Wallet wallet);

}
