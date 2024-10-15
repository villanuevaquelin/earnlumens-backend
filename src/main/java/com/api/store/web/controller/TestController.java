package com.api.store.web.controller;

import com.api.store.domain.service.StellarService;
import com.api.store.domain.service.WalletService;
import com.api.store.web.security.EncryptorDecoder;
import com.api.store.web.security.jwt.JwtUtils;
import com.api.store.web.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    WalletService walletService;

    @Autowired
    StellarService stellarService;

    @Autowired
    private EncryptorDecoder encryptorDecoder;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/readcors")
    public String readCors(@CookieValue(name = "HOT", defaultValue = "default-user-id") String httpOnlyCookieValue){
        return "HOT = " + httpOnlyCookieValue;
    }

    @GetMapping("/cookies")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String readAllCookies(
            @CookieValue(name = "HOT", defaultValue = "default-user-id") String httpOnlyCookieEncryptedValue) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(jwtUtils.hasValidHttpOnlyCookie(userDetails, httpOnlyCookieEncryptedValue)){
            return "Solicitud validada por JWT o Cookie Http Only";
        }else{
            return "-No cookies o cookie httpOnly diferente al jwt";
        }


//        String jwt = userDetails.getJwt();
//        System.out.println("JWT = "+ jwt);
//
//        String httpOnlyCookieValue = encryptorDecoder.decryptCookie(httpOnlyCookieEncryptedValue, userDetails.getXlmAddress());
//
//
//        System.out.println("HOT = " + httpOnlyCookieValue);
//
//        if(jwt.equals(httpOnlyCookieValue)){
//            System.out.println("Solicitud validada por JWT o Cookie Http Only");
//            return "Solicitud validada por JWT o Cookie Http Only";
//        }else {
//            System.out.println("En tu cara hacker");
//        }
//
//        return "No cookies o cookie httpOnly diferente al jwt";
    }


    @GetMapping("/balance")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String getBalance() throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String address = userDetails.getXlmAddress();
        System.out.println("Public Key = "+ address);
        return stellarService.getBalance(address);
    }

    @GetMapping("/pay")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String sendPayment() throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String address = userDetails.getXlmAddress();
        System.out.println("Public Key = "+ address);
        //return stellarService.sendSinglePayment(userDetails.getSecretKey(),"GCR2EDQTL3UQI7K3EZRAD5CBGXTB7B76FCIXRPB3NNGZZ4NJ25AYPLJI",
        //"333.339423","memorando");

        return stellarService.sendDualPayment(userDetails.getSecretKey(), "GCR2EDQTL3UQI7K3EZRAD5CBGXTB7B76FCIXRPB3NNGZZ4NJ25AYPLJI",
                "GCPLQDTQH7FD5U66TC7XUVXCAALPAWELIM6FNHOSBKTVP4A5DK53OUCX","0.06","memorando");

        //GCR2EDQTL3UQI7K3EZRAD5CBGXTB7B76FCIXRPB3NNGZZ4NJ25AYPLJI
        //GCPLQDTQH7FD5U66TC7XUVXCAALPAWELIM6FNHOSBKTVP4A5DK53OUCX
    }

/*    @GetMapping("/puk")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String publickey() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String publicKey;
        if (principal instanceof UserDetails) {
            String accessToken = encryptor.decryptUsername(((UserDetailsImpl)principal).getUsername());
            publicKey = walletService.findByAccessToken(encryptor.encryptAccessToken(accessToken)).get().getPublicKey();
        } else {
            publicKey = principal.toString();
        }
        return "Public key: "+publicKey;
    }

    @GetMapping("/pik")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String secretkey() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String secretKey;
        if (principal instanceof UserDetails) {
            String accessToken = encryptor.decryptUsername(((UserDetailsImpl)principal).getUsername());
            secretKey = walletService.findByAccessToken(encryptor.encryptAccessToken(accessToken)).get().getSecretKey();
        } else {
            secretKey = principal.toString();
        }
        return "Secret key: "+secretKey;
    }*/

    @GetMapping("/puk")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String publickey() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String publicKey;
        if (principal instanceof UserDetails) {
            String accessToken = encryptorDecoder.decryptUsername(((UserDetailsImpl)principal).getUsername())+((UserDetailsImpl)principal).getXlmAddress();
            publicKey = walletService.findByAccessToken(encryptorDecoder.encryptAccessToken(accessToken)).get().getPublicKey();
        } else {
            publicKey = principal.toString();
        }
        return "Public key: "+publicKey;
    }

    @GetMapping("/pik")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String secretkey() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String secretKey;
        if (principal instanceof UserDetails) {
            String accessToken = encryptorDecoder.decryptUsername(((UserDetailsImpl)principal).getUsername())+((UserDetailsImpl)principal).getXlmAddress();
            secretKey = walletService.findByAccessToken(encryptorDecoder.encryptAccessToken(accessToken)).get().getSecretKey();
        } else {
            secretKey = principal.toString();
        }
        return "Secret key: "+secretKey;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetailsImpl)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return "User Content: "+username;
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
