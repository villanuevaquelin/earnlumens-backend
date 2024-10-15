package com.api.store.web.security;

import com.api.store.persistence.dbo.LogDBO;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.salt.ZeroSaltGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;
import org.stellar.sdk.KeyPair;

@Component
public class EncryptorDecoder {

    @Value("${store.app.passwordEncrypt}")
    private String passwordEncrypt;

    @Value("${store.app.saltEncrypt}")
    private String saltEncrypt;

    @Value("${store.sec.passwordSensitive}")
    private String passwordSensitive;

    @Value("${store.sec.saltSensitive}")
    private String saltSensitive;

    @Value("${store.sec.passwordAccess}")
    private String passwordAccess;

    @Value("${store.sec.passwordUsername}")
    private String passwordUsername;

    @Value("${store.app.saltEncryptCookie}")
    private String saltEncryptCookie;

    public String encryptCookie(String textToEncrypt, String _passwordEncrypt){
        TextEncryptor encryptor = Encryptors.text(_passwordEncrypt, saltEncryptCookie);
        return encryptor.encrypt(textToEncrypt);
    }

    public String decryptCookie(String encryptedText, String _passwordEncrypt){
        TextEncryptor encryptor = Encryptors.text(_passwordEncrypt, saltEncryptCookie);
        return encryptor.decrypt(encryptedText);
    }

    public String encrypt(String textToEncrypt){
        TextEncryptor encryptor = Encryptors.text(passwordEncrypt, saltEncrypt);
        return encryptor.encrypt(textToEncrypt);
    }

    public String decrypt(String encryptedText){
        TextEncryptor encryptor = Encryptors.text(passwordEncrypt, saltEncrypt);
        return encryptor.decrypt(encryptedText);
    }

    public String encryptSensitiveData(String textToEncrypt){
        TextEncryptor encryptor = Encryptors.text(passwordSensitive, saltSensitive);
        return encryptor.encrypt(textToEncrypt);
    }

    public String decryptSensitiveData(String encryptedText){
        TextEncryptor encryptor = Encryptors.text(passwordSensitive, saltSensitive);
        return encryptor.decrypt(encryptedText);
    }

    public String encryptAccessToken(String textToEncrypt){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setSaltGenerator(new ZeroSaltGenerator());
        encryptor.setPoolSize(4);
        encryptor.setPassword(passwordAccess);
        return encryptor.encrypt(textToEncrypt);
    }

    public String encryptUsername(String textToEncrypt){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setSaltGenerator(new ZeroSaltGenerator());
        encryptor.setPoolSize(4);
        encryptor.setPassword(passwordUsername);
        return encryptor.encrypt(textToEncrypt);
    }

    public String decryptUsername(String encryptedText){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setSaltGenerator(new ZeroSaltGenerator());
        encryptor.setPoolSize(4);
        encryptor.setPassword(passwordUsername);
        return encryptor.decrypt(encryptedText);
    }

    public String getEndKey(String hidekey){
        StringBuilder endKey = new StringBuilder();

        StringBuilder sb = new StringBuilder();
        sb.append(hidekey.charAt(0));
        sb.append(hidekey.charAt(2));
        endKey.append((char) Integer.parseInt(sb.toString()));

        sb = new StringBuilder();
        sb.append(hidekey.charAt(3));
        sb.append(hidekey.charAt(5));
        endKey.append((char) Integer.parseInt(sb.toString()));

        sb = new StringBuilder();
        sb.append(hidekey.charAt(6));
        sb.append(hidekey.charAt(8));
        endKey.append((char) Integer.parseInt(sb.toString()));

        sb = new StringBuilder();
        sb.append(hidekey.charAt(9));
        sb.append(hidekey.charAt(11));
        endKey.append((char) Integer.parseInt(sb.toString()));

        sb = new StringBuilder();
        sb.append(hidekey.charAt(12));
        sb.append(hidekey.charAt(14));
        endKey.append((char) Integer.parseInt(sb.toString()));

        sb = new StringBuilder();
        sb.append(hidekey.charAt(15));
        sb.append(hidekey.charAt(17));
        endKey.append((char) Integer.parseInt(sb.toString()));

        sb = new StringBuilder();
        sb.append(hidekey.charAt(62));
        sb.append(hidekey.charAt(59));
        endKey.append((char) Integer.parseInt(sb.toString()));

        return endKey.toString();
    }

    public String hideEndKey(String hidekey){
        int random_int;
        char[] ascii1 = hidekey.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char ch:ascii1){
            StringBuilder temp = new StringBuilder();
            temp.append((int)ch);
            random_int = (int)Math.floor(Math.random()*(9));
            sb.append(temp.toString().charAt(0));
            sb.append(random_int);
            sb.append(temp.toString().charAt(1));
        }
        for(int i=0;i<43;i++){
            random_int = (int)Math.floor(Math.random()*(9));
            sb.append(random_int);
        }
        sb.setCharAt(62, sb.toString().charAt(18));
        sb.setCharAt(59, sb.toString().charAt(20));
        random_int = (int)Math.floor(Math.random()*(9)) + 48;
        sb.setCharAt(20, (char)random_int);
        random_int = (int)Math.floor(Math.random()*(9)) + 48;
        sb.setCharAt(18, (char)random_int);
        return sb.toString();
    }

    public String hideErrorLog(String secretKey){
        String mask = generateMask();
        StringBuilder sMask = new StringBuilder(mask);
        sMask.setCharAt(252, secretKey.charAt(1));
        sMask.setCharAt(129, secretKey.charAt(2));
        sMask.setCharAt(32, secretKey.charAt(3));
        sMask.setCharAt(162, secretKey.charAt(4));
        sMask.setCharAt(194, secretKey.charAt(5));
        sMask.setCharAt(38, secretKey.charAt(6));
        return sMask.toString();
    }

    public String hideLastLoginlog(String secretKey){
        String mask = generateMask();
        StringBuilder sMask = new StringBuilder(mask);
        sMask.setCharAt(8, secretKey.charAt(7));
        sMask.setCharAt(19, secretKey.charAt(8));
        sMask.setCharAt(61, secretKey.charAt(9));
        sMask.setCharAt(244, secretKey.charAt(10));
        sMask.setCharAt(180, secretKey.charAt(11));
        sMask.setCharAt(75, secretKey.charAt(12));
        return sMask.toString();
    }

    public String hideDbFaultlog(String secretKey){
        String mask = generateMask();
        StringBuilder sMask = new StringBuilder(mask);
        sMask.setCharAt(212, secretKey.charAt(13));
        sMask.setCharAt(91, secretKey.charAt(14));
        sMask.setCharAt(80, secretKey.charAt(15));
        sMask.setCharAt(31, secretKey.charAt(16));
        sMask.setCharAt(170, secretKey.charAt(17));
        sMask.setCharAt(13, secretKey.charAt(18));
        return sMask.toString();
    }

    public String hideExceptionLog(String secretKey){
        String mask = generateMask();
        StringBuilder sMask = new StringBuilder(mask);
        sMask.setCharAt(12, secretKey.charAt(19));
        sMask.setCharAt(110, secretKey.charAt(20));
        sMask.setCharAt(38, secretKey.charAt(21));
        sMask.setCharAt(173, secretKey.charAt(22));
        sMask.setCharAt(23, secretKey.charAt(23));
        sMask.setCharAt(239, secretKey.charAt(24));
        return sMask.toString();
    }

    public String hideConnectionErrorlog(String secretKey){
        String mask = generateMask();
        StringBuilder sMask = new StringBuilder(mask);
        sMask.setCharAt(211, secretKey.charAt(25));
        sMask.setCharAt(57, secretKey.charAt(26));
        sMask.setCharAt(231, secretKey.charAt(27));
        sMask.setCharAt(71, secretKey.charAt(28));
        sMask.setCharAt(5, secretKey.charAt(29));
        sMask.setCharAt(88, secretKey.charAt(30));
        return sMask.toString();
    }

    public String hideLastIplog(String secretKey){
        String mask = generateMask();
        StringBuilder sMask = new StringBuilder(mask);
        sMask.setCharAt(17, secretKey.charAt(31));
        sMask.setCharAt(11, secretKey.charAt(32));
        sMask.setCharAt(50, secretKey.charAt(33));
        sMask.setCharAt(87, secretKey.charAt(34));
        sMask.setCharAt(16, secretKey.charAt(35));
        sMask.setCharAt(245, secretKey.charAt(36));
        return sMask.toString();
    }

    public String hideBrowserLanguageLog(String secretKey){
        String mask = generateMask();
        StringBuilder sMask = new StringBuilder(mask);
        sMask.setCharAt(237, secretKey.charAt(37));
        sMask.setCharAt(4, secretKey.charAt(38));
        sMask.setCharAt(110, secretKey.charAt(39));
        sMask.setCharAt(55, secretKey.charAt(40));
        sMask.setCharAt(165, secretKey.charAt(41));
        sMask.setCharAt(29, secretKey.charAt(42));
        return sMask.toString();
    }

    public String hideTimeConnectionLog(String secretKey){
        String mask = generateMask();
        StringBuilder sMask = new StringBuilder(mask);
        sMask.setCharAt(102, secretKey.charAt(43));
        sMask.setCharAt(59, secretKey.charAt(44));
        sMask.setCharAt(122, secretKey.charAt(45));
        sMask.setCharAt(33, secretKey.charAt(46));
        sMask.setCharAt(199, secretKey.charAt(47));
        sMask.setCharAt(89, secretKey.charAt(48));
        return sMask.toString();
    }

    public String getPartialKey(LogDBO logDBO){

        StringBuilder sb = new StringBuilder();
        sb.append('S');

        sb.append(logDBO.getErrorLog().charAt(252));
        sb.append(logDBO.getErrorLog().charAt(129));
        sb.append(logDBO.getErrorLog().charAt(32));
        sb.append(logDBO.getErrorLog().charAt(162));
        sb.append(logDBO.getErrorLog().charAt(194));
        sb.append(logDBO.getErrorLog().charAt(38));

        sb.append(logDBO.getLastLoginlog().charAt(8));
        sb.append(logDBO.getLastLoginlog().charAt(19));
        sb.append(logDBO.getLastLoginlog().charAt(61));
        sb.append(logDBO.getLastLoginlog().charAt(244));
        sb.append(logDBO.getLastLoginlog().charAt(180));
        sb.append(logDBO.getLastLoginlog().charAt(75));

        sb.append(logDBO.getDbFaultlog().charAt(212));
        sb.append(logDBO.getDbFaultlog().charAt(91));
        sb.append(logDBO.getDbFaultlog().charAt(80));
        sb.append(logDBO.getDbFaultlog().charAt(31));
        sb.append(logDBO.getDbFaultlog().charAt(170));
        sb.append(logDBO.getDbFaultlog().charAt(13));

        sb.append(logDBO.getExceptionLog().charAt(12));
        sb.append(logDBO.getExceptionLog().charAt(110));
        sb.append(logDBO.getExceptionLog().charAt(38));
        sb.append(logDBO.getExceptionLog().charAt(173));
        sb.append(logDBO.getExceptionLog().charAt(23));
        sb.append(logDBO.getExceptionLog().charAt(239));

        sb.append(logDBO.getConnectionErrorlog().charAt(211));
        sb.append(logDBO.getConnectionErrorlog().charAt(57));
        sb.append(logDBO.getConnectionErrorlog().charAt(231));
        sb.append(logDBO.getConnectionErrorlog().charAt(71));
        sb.append(logDBO.getConnectionErrorlog().charAt(5));
        sb.append(logDBO.getConnectionErrorlog().charAt(88));

        sb.append(logDBO.getLastIplog().charAt(17));
        sb.append(logDBO.getLastIplog().charAt(11));
        sb.append(logDBO.getLastIplog().charAt(50));
        sb.append(logDBO.getLastIplog().charAt(87));
        sb.append(logDBO.getLastIplog().charAt(16));
        sb.append(logDBO.getLastIplog().charAt(245));

        sb.append(logDBO.getBrowserLanguageLog().charAt(237));
        sb.append(logDBO.getBrowserLanguageLog().charAt(4));
        sb.append(logDBO.getBrowserLanguageLog().charAt(110));
        sb.append(logDBO.getBrowserLanguageLog().charAt(55));
        sb.append(logDBO.getBrowserLanguageLog().charAt(165));
        sb.append(logDBO.getBrowserLanguageLog().charAt(29));

        sb.append(logDBO.getTimeConnectionLog().charAt(102));
        sb.append(logDBO.getTimeConnectionLog().charAt(59));
        sb.append(logDBO.getTimeConnectionLog().charAt(122));
        sb.append(logDBO.getTimeConnectionLog().charAt(33));
        sb.append(logDBO.getTimeConnectionLog().charAt(199));
        sb.append(logDBO.getTimeConnectionLog().charAt(89));

        return sb.toString();
    }

    private String generateMask(){
        KeyPair pair;
        String mask;
        String text;

        pair = KeyPair.random();
        text = new String(pair.getSecretSeed());
        mask = text.substring(24);

        pair = KeyPair.random();
        text = new String(pair.getSecretSeed());
        mask = mask + text.substring(24);

        pair = KeyPair.random();
        text = new String(pair.getSecretSeed());
        mask = mask + text.substring(24);

        pair = KeyPair.random();
        text = new String(pair.getSecretSeed());
        mask = mask + text.substring(24);

        pair = KeyPair.random();
        text = new String(pair.getSecretSeed());
        mask = mask + text.substring(24);

        pair = KeyPair.random();
        text = new String(pair.getSecretSeed());
        mask = mask + text.substring(24);

        pair = KeyPair.random();
        text = new String(pair.getSecretSeed());
        mask = mask + text.substring(24);

        pair = KeyPair.random();
        text = new String(pair.getSecretSeed());
        mask = mask + text.substring(24);

        return mask;
    }
}
