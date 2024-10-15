package com.api.store.domain.service;

import com.api.store.domain.Feedback;
import com.api.store.domain.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;


    public Feedback save(Feedback feedback){
        return feedbackRepository.save(feedback);
    }
}
