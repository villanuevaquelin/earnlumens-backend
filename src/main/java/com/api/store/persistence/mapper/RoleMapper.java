package com.api.store.persistence.mapper;

import com.api.store.domain.Role;
import com.api.store.persistence.dbo.RoleDBO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {ERoleMapper.class})
public interface RoleMapper {

    Role toRole(RoleDBO roleDBO);

    @InheritInverseConfiguration
    RoleDBO toRoleDBO(Role role);
}
