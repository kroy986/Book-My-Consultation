package com.book.consultant.appointment.service.repository;

import com.book.consultant.appointment.service.collection.DoctorEntity;
import com.book.consultant.appointment.service.collection.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepo extends MongoRepository<Prescription, String> {
}
