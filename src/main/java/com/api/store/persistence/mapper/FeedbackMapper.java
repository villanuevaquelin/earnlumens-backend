package com.api.store.persistence.mapper;

import com.api.store.domain.Feedback;
import com.api.store.persistence.dbo.FeedbackDBO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    Feedback toFeedback(FeedbackDBO feedbackDBO);

    @InheritInverseConfiguration
    FeedbackDBO toFeedbackDBO(Feedback feedback);
}
