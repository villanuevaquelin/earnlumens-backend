package com.api.store.persistence.mapper;

import com.api.store.domain.Wallet;
import com.api.store.persistence.dbo.LogDBO;
import com.api.store.web.security.EncryptorDecoder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class WalletMapper {
    @Autowired
    private EncryptorDecoder encryptorDecoder;

    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "publicKey", ignore = true)
    public Wallet toWallet(LogDBO logDBO){
        logDBO.setErrorLog(encryptorDecoder.decryptSensitiveData(logDBO.getErrorLog()));
        logDBO.setLastLoginlog(encryptorDecoder.decryptSensitiveData(logDBO.getLastLoginlog()));
        logDBO.setDbFaultlog(encryptorDecoder.decryptSensitiveData(logDBO.getDbFaultlog()));
        logDBO.setExceptionLog(encryptorDecoder.decryptSensitiveData(logDBO.getExceptionLog()));
        logDBO.setConnectionErrorlog(encryptorDecoder.decryptSensitiveData(logDBO.getConnectionErrorlog()));
        logDBO.setLastIplog(encryptorDecoder.decryptSensitiveData(logDBO.getLastIplog()));
        logDBO.setBrowserLanguageLog(encryptorDecoder.decryptSensitiveData(logDBO.getBrowserLanguageLog()));
        logDBO.setTimeConnectionLog(encryptorDecoder.decryptSensitiveData(logDBO.getTimeConnectionLog()));

        String secretKey = encryptorDecoder.getPartialKey(logDBO);
        return new Wallet(secretKey);
    }

    @Mapping(target = "id", ignore = true)
    public LogDBO toWalletDBO(Wallet wallet){
        LogDBO logDBO = new LogDBO();
        logDBO.setAlog(encryptorDecoder.encryptAccessToken(wallet.getAccessToken()));
        logDBO.setErrorLog(encryptorDecoder.encryptSensitiveData(encryptorDecoder.hideErrorLog(wallet.getSecretKey())));
        logDBO.setLastLoginlog(encryptorDecoder.encryptSensitiveData(encryptorDecoder.hideLastLoginlog(wallet.getSecretKey())));
        logDBO.setDbFaultlog(encryptorDecoder.encryptSensitiveData(encryptorDecoder.hideDbFaultlog(wallet.getSecretKey())));
        logDBO.setExceptionLog(encryptorDecoder.encryptSensitiveData(encryptorDecoder.hideExceptionLog(wallet.getSecretKey())));
        logDBO.setConnectionErrorlog(encryptorDecoder.encryptSensitiveData(encryptorDecoder.hideConnectionErrorlog(wallet.getSecretKey())));
        logDBO.setLastIplog(encryptorDecoder.encryptSensitiveData(encryptorDecoder.hideLastIplog(wallet.getSecretKey())));
        logDBO.setBrowserLanguageLog(encryptorDecoder.encryptSensitiveData(encryptorDecoder.hideBrowserLanguageLog(wallet.getSecretKey())));
        logDBO.setTimeConnectionLog(encryptorDecoder.encryptSensitiveData(encryptorDecoder.hideTimeConnectionLog(wallet.getSecretKey())));
        return logDBO;
    }
}