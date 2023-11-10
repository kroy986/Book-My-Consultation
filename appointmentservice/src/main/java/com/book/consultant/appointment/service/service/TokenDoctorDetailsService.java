package com.book.consultant.appointment.service.service;

import com.book.consultant.appointment.service.collection.DoctorEntity;
import com.book.consultant.appointment.service.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenDoctorDetailsService implements UserDetailsService {
    @Autowired
    DoctorRepo doctorRepo;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<DoctorEntity> doctorEntity = doctorRepo.findByEmailId(username);

        if (doctorEntity.isPresent()) {
            DoctorEntity user = doctorEntity.get();
            return new DoctorDetailsService(user);
        }
        return null;
    }
}
