package com.example.crawler.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class CovidTrackingDTO {
    public int date;
    public String state;
    public int positive;
}
