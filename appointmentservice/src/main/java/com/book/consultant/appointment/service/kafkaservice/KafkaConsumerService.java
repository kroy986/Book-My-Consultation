package com.book.consultant.appointment.service.kafkaservice;

import com.book.consultant.appointment.service.collection.Appointment;
import com.book.consultant.appointment.service.constants.GlobalConstant;
import com.book.consultant.appointment.service.responsemodel.PaymentDetailsObject;
import com.book.consultant.appointment.service.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class KafkaConsumerService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AppointmentService appointmentService;


    @KafkaListener(topics = "topic12", groupId = "group12")
    public void consumePaymentDetailsMessage(String message) throws IOException {
        if (message != null) {
            log.info("inside consumePaymentDetailsMessage method :: message :: "+ message);
            PaymentDetailsObject paymentDetailsObject = objectMapper.readValue(message, PaymentDetailsObject.class);
            Appointment appointment = appointmentService.getAppointmentDetailsByAppointmentId(paymentDetailsObject.getAppointmentId());
            appointment.setStatus(GlobalConstant.CONFIRMED_STATUS);
            appointmentService.updateAppointmentDetails(appointment);
        }
    }
}
