package com.doctor.book.consultant.payment.service.feignclientinterface;


import com.doctor.book.consultant.payment.service.collection.Appointment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "API-GATEWAY")
public interface PaymentInterface {
    @GetMapping("/api/appointments/{appointmentId}")
    public Appointment getAppointmentDetailsByAppointmentId(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                                            @PathVariable(value = "appointmentId") String appointmentId);

}
