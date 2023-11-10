package com.doctor.book.consultant.doctor.server.service;

import com.doctor.book.consultant.doctor.server.collection.DoctorEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DoctorDetailsService  implements UserDetails {

    DoctorEntity doctorEntity;
    public DoctorDetailsService(DoctorEntity doctorEntity){
        this.doctorEntity  = doctorEntity;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities
                = new ArrayList<>();
        for (String role: doctorEntity.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return doctorEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return doctorEntity.getEmailId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
