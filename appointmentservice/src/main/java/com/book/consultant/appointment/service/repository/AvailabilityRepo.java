package com.book.consultant.appointment.service.repository;

import com.book.consultant.appointment.service.collection.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvailabilityRepo extends MongoRepository<Availability, String> {
    Optional<Availability> findByDoctorId(String doctorId);
}
