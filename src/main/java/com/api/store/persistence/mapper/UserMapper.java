package com.api.store.persistence.mapper;

import com.api.store.domain.User;
import com.api.store.persistence.dbo.UserDBO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "secretKey", ignore = true)
    @Mapping(target = "jwt", ignore = true)
    User toUser(UserDBO userDBO);

    @InheritInverseConfiguration
    UserDBO toUserDBO(User user);

}
