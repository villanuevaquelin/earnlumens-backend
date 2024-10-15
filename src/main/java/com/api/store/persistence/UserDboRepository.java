package com.api.store.persistence;

import com.api.store.domain.ERole;
import com.api.store.domain.Role;
import com.api.store.domain.User;
import com.api.store.domain.repository.UserRepository;
import com.api.store.persistence.crud.RoleDatabaseRepository;
import com.api.store.persistence.crud.UserDatabaseRepository;
import com.api.store.persistence.dbo.RoleDBO;
import com.api.store.persistence.dbo.UserDBO;
import com.api.store.persistence.mapper.ERoleMapper;
import com.api.store.persistence.mapper.RoleMapper;
import com.api.store.persistence.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDboRepository implements UserRepository {

    @Autowired
    private UserDatabaseRepository userDatabaseRepository;

    @Autowired
    private RoleDatabaseRepository roleDatabaseRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private ERoleMapper eRoleMapper;

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<UserDBO> userDBO = userDatabaseRepository.findByUsername(username);
        return userDBO.map(usr -> userMapper.toUser(usr));
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userDatabaseRepository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        UserDBO userDBO = userMapper.toUserDBO(user);
        return userMapper.toUser(userDatabaseRepository.save(userDBO));
    }

    @Override
    public Optional<Role> findByRoleName(ERole name) {
        Optional<RoleDBO> roleDBO = roleDatabaseRepository.findByName(eRoleMapper.toERoleDBO(name));
        return roleDBO.map(rle -> roleMapper.toRole(rle));
    }


}
