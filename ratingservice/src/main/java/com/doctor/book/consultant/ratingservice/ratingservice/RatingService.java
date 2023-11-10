package com.doctor.book.consultant.ratingservice.ratingservice;

import com.doctor.book.consultant.ratingservice.collection.Rating;
import com.doctor.book.consultant.ratingservice.repository.RatingRepo;
import com.doctor.book.consultant.ratingservice.requestmodel.DoctorEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {
    @Autowired
    private RatingRepo ratingRepo;

    @Autowired
    private ObjectMapper objectMapper;

    public String convertToString(Object rqst) {
        try {
            return objectMapper.writeValueAsString(rqst);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Rating saveRatingDetails(Rating rating) {
        return ratingRepo.save(rating);
    }
}
