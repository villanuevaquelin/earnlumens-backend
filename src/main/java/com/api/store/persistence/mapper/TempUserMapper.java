package com.api.store.persistence.mapper;

import com.api.store.domain.TempUser;
import com.api.store.persistence.dbo.TempUserDBO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TempUserMapper {

    TempUser toTempUser(TempUserDBO tempUserDBO);


    @InheritInverseConfiguration
    TempUserDBO toTempUserDBO(TempUser tempUser);
}
