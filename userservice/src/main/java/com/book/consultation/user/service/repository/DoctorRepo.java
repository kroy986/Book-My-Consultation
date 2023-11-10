package com.book.consultation.user.service.repository;

import com.book.consultation.user.service.collection.DoctorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepo extends MongoRepository<DoctorEntity, String> {
    Optional<DoctorEntity> findByEmailId(String username);
}
