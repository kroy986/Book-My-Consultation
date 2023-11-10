package com.book.consultant.appointment.service.feignclientinterface;

import com.book.consultant.appointment.service.collection.DoctorEntity;
import com.book.consultant.appointment.service.collection.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "API-GATEWAY")
public interface AppointmentInterface {
    @GetMapping("/api/doctors/{doctorId}")
    public DoctorEntity getDoctorDetailsByDoctorId(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                                   @PathVariable(value = "doctorId") String doctorId);
    @GetMapping("/api/users/{userId}")
    public UserEntity getUserDetailsByUserId(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                             @PathVariable(value = "userId") String userId);
}
