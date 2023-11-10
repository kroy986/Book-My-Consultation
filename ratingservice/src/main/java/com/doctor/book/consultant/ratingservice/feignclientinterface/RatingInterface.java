package com.doctor.book.consultant.ratingservice.feignclientinterface;


import com.doctor.book.consultant.ratingservice.requestmodel.DoctorEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "API-GATEWAY")
public interface RatingInterface {
    @GetMapping("/api/doctors/{doctorId}")
    public DoctorEntity getDoctorDetailsByDoctorId(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                                   @PathVariable(value = "doctorId") String doctorId);

}
