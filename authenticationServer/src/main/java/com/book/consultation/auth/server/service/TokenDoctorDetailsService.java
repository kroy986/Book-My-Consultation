package com.book.consultation.auth.server.service;


import com.book.consultation.auth.server.collection.DoctorEntity;
import com.book.consultation.auth.server.collection.UserEntity;
import com.book.consultation.auth.server.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TokenDoctorDetailsService implements UserDetailsService {
    @Autowired
    DoctorRepo doctorRepo;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<DoctorEntity> doctorEntity = doctorRepo.findByEmailId(username);
        if (doctorEntity.isPresent()) {
            DoctorEntity user = doctorEntity.get();
            return new User(user.getEmailId(),
                    user.getPassword(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Doctor not found with username: " + username);
        }
    }
}
