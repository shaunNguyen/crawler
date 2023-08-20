package com.example.crawler.service;

import com.example.crawler.model.CovidTrackingModel;
import com.example.crawler.repository.CovidTrackingRepository;
import com.example.crawler.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import com.example.crawler.dto.CovidTrackingDTO;

//@Service
@Component
public class CrawlerService {
    @Value("${covidTracking}")
    public String covidTracking;

    @Autowired
    CovidTrackingRepository covidTrackingRepository;

    @Autowired
    ProducerRepository producerRepository;

    //@PostConstruct
    //@Scheduled(fixedRate = 1)
    @Scheduled(fixedRateString = "${schedule.fixed.time.mil.seconds}")
    public void getAllCovidDTO() {
        //producerRepository.sendMessage("demo1703");

        RestTemplate restTemplate = new RestTemplate();
        CovidTrackingDTO[] covidTrackingDTO = restTemplate.getForObject(covidTracking, CovidTrackingDTO[].class);

        int count = 0;
        if (covidTrackingDTO != null) {
            for (CovidTrackingDTO covid : covidTrackingDTO) {
                System.out.printf("hello %d \n", count);
                count++;
                System.out.printf("state %s, date %d, positive %d\n", covid.state, covid.date, covid.positive);
                String id = Integer.toString(count);
                covidTrackingRepository.save(new CovidTrackingModel(id, covid.state, covid.date, covid.positive));
                String message = "id:" + id + ",state:" + covid.state + ",positive:" + Integer.toString(covid.positive);
                producerRepository.sendMessage(message);
            }
        }

    }

}
