package com.example.crawler.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.lang.annotation.Documented;

@Data
@AllArgsConstructor
@Document("CovidTracking")
public class CovidTrackingModel {
    @Id
    private String id;
    private String state;
    private int date;
    private int positive;


}
