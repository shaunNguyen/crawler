package com.example.crawler.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.crawler.model.CovidTrackingModel;

public interface CovidTrackingRepository extends MongoRepository<CovidTrackingModel, String> {
}