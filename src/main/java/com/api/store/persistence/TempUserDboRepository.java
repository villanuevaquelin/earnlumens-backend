package com.api.store.persistence;

import com.api.store.domain.TempUser;
import com.api.store.domain.repository.TempUserRepository;
import com.api.store.persistence.crud.TempUserDatabaseRepository;
import com.api.store.persistence.dbo.TempUserDBO;
import com.api.store.persistence.mapper.TempUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TempUserDboRepository implements TempUserRepository {

    @Autowired
    private TempUserDatabaseRepository tempUserDatabaseRepository;

    @Autowired
    private TempUserMapper tempUserMapper;

    @Override
    public Optional<TempUser> findByUsername(String username) {
        Optional<TempUserDBO> tempUserDBO = tempUserDatabaseRepository.findByUsername(username);
        return tempUserDBO.map(usr -> tempUserMapper.toTempUser(usr));
    }

    @Override
    public Boolean existsByUsername(String username) {
        return tempUserDatabaseRepository.existsByUsername(username);
    }

    @Override
    public TempUser save(TempUser tempUser) {
        TempUserDBO tempUserDBO = tempUserMapper.toTempUserDBO(tempUser);
        return tempUserMapper.toTempUser(tempUserDatabaseRepository.save(tempUserDBO));
    }

    @Override
    public void deleteById(String id) {
        tempUserDatabaseRepository.deleteById(id);
    }


}
