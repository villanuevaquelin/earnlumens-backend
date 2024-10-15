package com.api.store.web.security.services;

import com.api.store.domain.ERole;
import com.api.store.domain.Role;
import com.api.store.domain.User;
import com.api.store.domain.repository.UserRepository;
import com.api.store.domain.repository.WalletRepository;
import com.api.store.web.security.EncryptorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.StringTokenizer;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    EncryptorDecoder encryptorDecoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Username -= "+username);
        User user = userRepository.findByUsername(encryptorDecoder.encryptUsername(parseUsername(username)))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        // busca en repositorio mongodb y lo castea a un UserDetails (usuario de contexto)
        String accessToken = encryptorDecoder.decryptUsername(user.getUsername())+ encryptorDecoder.decrypt(user.getWpk());
        String partialKey = walletRepository.findByAccessToken(encryptorDecoder.encryptAccessToken(accessToken)).get().getSecretKey();
        String hiddenEndKey = encryptorDecoder.decrypt(user.getTimeZone());
        String endKey = encryptorDecoder.getEndKey(hiddenEndKey);
        user.setSecretKey(partialKey+endKey);
        user.setWpk(encryptorDecoder.decrypt(user.getWpk()));
        user.setJwt(parseJwt(username));
        return UserDetailsImpl.build(user);
    }

    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public Optional<Role> findByRoleName(ERole name){
        return userRepository.findByRoleName(name);
    }

    private String parseUsername(String usernameAndJwt)
    {
        StringTokenizer st = new StringTokenizer(usernameAndJwt, "\n");
        if(st.hasMoreElements())
            return st.nextToken();
        else
            return "";
    }

    private String parseJwt(String usernameAndJwt)
    {
        StringTokenizer st = new StringTokenizer(usernameAndJwt, "\n");
        if(st.hasMoreElements())
            st.nextToken();
        if(st.hasMoreElements())
            return st.nextToken();
        else
            return "";
    }
}
