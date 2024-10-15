package com.api.store.persistence;

import com.api.store.domain.Wallet;
import com.api.store.domain.repository.WalletRepository;
import com.api.store.persistence.crud.WalletDatabaseRepository;
import com.api.store.persistence.dbo.LogDBO;
import com.api.store.persistence.mapper.WalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WalletDboRepository implements WalletRepository {
    @Autowired
    private WalletDatabaseRepository walletDatabaseRepository;

    @Autowired
    private WalletMapper walletMapper;

    @Override
    public Optional<Wallet> findByAccessToken(String accessToken) {
        Optional<LogDBO> walletDBO = walletDatabaseRepository.findByAlog(accessToken);
        return walletDBO.map(wlt -> walletMapper.toWallet(wlt));
    }

    @Override
    public Wallet save(Wallet wallet){
        LogDBO logDBO = walletMapper.toWalletDBO(wallet);
        return walletMapper.toWallet(walletDatabaseRepository.save(logDBO));
    }
}
