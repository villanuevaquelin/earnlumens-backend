package com.api.store.persistence.mapper;

import com.api.store.domain.Founder;
import com.api.store.persistence.dbo.FounderDBO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FounderMapper {

    Founder toFounder(FounderDBO founderDBO);

    @InheritInverseConfiguration
    FounderDBO toFounderDBO(Founder founder);
}
