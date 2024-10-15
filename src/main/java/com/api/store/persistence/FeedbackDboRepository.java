package com.api.store.persistence;

import com.api.store.domain.Feedback;
import com.api.store.domain.repository.FeedbackRepository;
import com.api.store.persistence.crud.FeedbackDatabaseRepository;
import com.api.store.persistence.dbo.FeedbackDBO;
import com.api.store.persistence.mapper.FeedbackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FeedbackDboRepository implements FeedbackRepository {
    @Autowired
    private FeedbackDatabaseRepository feedbackDatabaseRepository;
    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public Feedback save(Feedback feedback) {
        FeedbackDBO feedbackDBO = feedbackMapper.toFeedbackDBO(feedback);
        return feedbackMapper.toFeedback(feedbackDatabaseRepository.save(feedbackDBO));
    }
}
