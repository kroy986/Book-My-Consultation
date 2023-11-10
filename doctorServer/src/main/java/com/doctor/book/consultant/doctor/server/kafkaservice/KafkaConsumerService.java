package com.doctor.book.consultant.doctor.server.kafkaservice;

import com.doctor.book.consultant.doctor.server.collection.DoctorEntity;
import com.doctor.book.consultant.doctor.server.repository.DoctorRepo;
import com.doctor.book.consultant.doctor.server.responsemodel.Rating;
import com.doctor.book.consultant.doctor.server.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class KafkaConsumerService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DoctorService doctorService;

    @Autowired
    DoctorRepo doctorRepo;

    @KafkaListener(topics = "topic13", groupId = "group13")
    public void consumeRatingDetailsMessage(String message) throws IOException {
        if (message != null) {
            log.info("inside consumeRatingDetailsMessage method :: message :: "+ message);
            Rating ratingDetailsObject = objectMapper.readValue(message, Rating.class);
            DoctorEntity doctorEntity = doctorService.getDoctorDetailsByDoctorId(ratingDetailsObject.getDoctorId());
            doctorEntity.setRating(ratingDetailsObject.getRating());
            doctorRepo.save(doctorEntity);
        }
    }
}
