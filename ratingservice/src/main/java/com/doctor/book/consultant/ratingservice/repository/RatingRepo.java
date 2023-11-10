package com.doctor.book.consultant.ratingservice.repository;

import com.doctor.book.consultant.ratingservice.collection.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepo extends MongoRepository<Rating, String> {
}
