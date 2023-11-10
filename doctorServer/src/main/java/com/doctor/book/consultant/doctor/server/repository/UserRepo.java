package com.doctor.book.consultant.doctor.server.repository;

import com.doctor.book.consultant.doctor.server.collection.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByEmailId(String username);
}
