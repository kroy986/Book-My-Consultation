package com.doctor.book.consultant.ratingservice.controller;

import com.doctor.book.consultant.ratingservice.collection.Rating;
import com.doctor.book.consultant.ratingservice.feignclientinterface.RatingInterface;
import com.doctor.book.consultant.ratingservice.ratingservice.RatingService;
import com.doctor.book.consultant.ratingservice.requestmodel.DoctorEntity;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RatingController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private RatingInterface ratingInterface;

    @Autowired
    private RatingService ratingService;

    @PostMapping("/ratings")
    public ResponseEntity getRatingDetails(@RequestBody Rating rating) {

        Rating ratingObject = ratingService.saveRatingDetails(rating);
        String ratingDetailsAsString = ratingService.convertToString(ratingObject);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic13",
                "message", ratingDetailsAsString);
        kafkaProducer.send(producerRecord);
        kafkaProducer.flush();
        return ResponseEntity.ok().body(ratingObject);
    }

}
