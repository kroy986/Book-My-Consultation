package com.doctor.book.consultant.doctor.server.repository;

import com.doctor.book.consultant.doctor.server.collection.DoctorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends MongoRepository<DoctorEntity, String> {
    Optional<DoctorEntity> findByEmailId(String username);
    Page<DoctorEntity> findByStatus(String status, Pageable pageable);

    Page<DoctorEntity> findBySpeciality(String pending, PageRequest pageRequest);

    Page<DoctorEntity> findByStatusAndSpeciality(String status, String speciality, PageRequest pageRequest);
}
