package com.api.store.persistence.mapper;

import com.api.store.domain.ERole;
import com.api.store.persistence.dbo.ERoleDBO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ERoleMapper {

    ERole toERole(ERoleDBO eRoleDBO);

    @InheritInverseConfiguration
    ERoleDBO toERoleDBO(ERole cheese);
}
