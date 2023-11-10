package com.book.consultation.user.service.service;

import com.book.consultation.user.service.collection.UserEntity;
import com.book.consultation.user.service.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepo.findByEmailId(username);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            return new UserDtlsService(user);
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
