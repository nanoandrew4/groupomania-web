package com.greenapper.repositories.mongo;

import com.greenapper.models.LogMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogMessageRepository extends MongoRepository<LogMessage, String> {
}
