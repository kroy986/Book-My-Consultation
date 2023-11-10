package com.doctor.book.consultant.payment.service.paymentservice;

import com.doctor.book.consultant.payment.service.collection.Appointment;
import com.doctor.book.consultant.payment.service.collection.PaymentDetails;
import com.doctor.book.consultant.payment.service.repository.PaymentRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private ObjectMapper objectMapper;

    public PaymentDetails savePaymentDetails(Appointment appointment) {
        PaymentDetails paymentDtlObject = PaymentDetails.builder()
                .createdDate(Calendar.getInstance().getTime())
                .appointmentId(appointment.getId()).build();

        return paymentRepo.save(paymentDtlObject);
    }

    public String convertToString(Object rqst) {
        try {
            return objectMapper.writeValueAsString(rqst);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
