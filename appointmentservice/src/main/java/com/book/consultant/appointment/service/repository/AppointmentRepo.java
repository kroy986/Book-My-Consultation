package com.book.consultant.appointment.service.repository;

import com.book.consultant.appointment.service.collection.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepo extends MongoRepository<Appointment, String> {
    Optional<List<Appointment>> findByUserId(String s);
}
