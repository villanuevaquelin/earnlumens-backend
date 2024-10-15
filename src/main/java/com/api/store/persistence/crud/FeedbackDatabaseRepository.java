package com.api.store.persistence.crud;

import com.api.store.persistence.dbo.FeedbackDBO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackDatabaseRepository extends MongoRepository<FeedbackDBO, String> {


}
