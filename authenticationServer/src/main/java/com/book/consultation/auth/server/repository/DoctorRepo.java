package com.book.consultation.auth.server.repository;

import com.book.consultation.auth.server.collection.DoctorEntity;
import com.book.consultation.auth.server.collection.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepo extends MongoRepository<DoctorEntity, String> {
    Optional<DoctorEntity> findByEmailId(String username);
}
