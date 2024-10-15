package com.api.store.domain.service;

import com.api.store.domain.Wallet;
import com.api.store.domain.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;


    public Optional<Wallet> findByAccessToken(String accessToken){
        return walletRepository.findByAccessToken(accessToken);
    }

    public Wallet save(Wallet wallet){
        return walletRepository.save(wallet);
    }
}
