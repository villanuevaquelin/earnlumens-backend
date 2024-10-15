package com.api.store.domain.service;

import com.api.store.domain.TempUser;
import com.api.store.domain.repository.TempUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TempUserService {

    @Autowired
    private TempUserRepository tempUserRepository;

    public Optional<TempUser> findByUsername(String username){
        return tempUserRepository.findByUsername(username);
    }

    public Boolean existsByUsername(String username){
        return tempUserRepository.existsByUsername(username);
    }

    public TempUser save(TempUser tempUser){
        return tempUserRepository.save(tempUser);
    }

    public void deleteById(String id){
        tempUserRepository.deleteById(id);
    }

}
