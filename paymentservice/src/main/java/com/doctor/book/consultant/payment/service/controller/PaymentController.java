package com.doctor.book.consultant.payment.service.controller;

import com.doctor.book.consultant.payment.service.collection.Appointment;
import com.doctor.book.consultant.payment.service.collection.PaymentDetails;
import com.doctor.book.consultant.payment.service.feignclientinterface.PaymentInterface;
import com.doctor.book.consultant.payment.service.paymentservice.PaymentService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private PaymentInterface paymentInterface;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity getPaymentDetails(@RequestParam(value = "appointmentId") String appointmentId,
                                            @RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        Appointment appointment = paymentInterface.getAppointmentDetailsByAppointmentId(authorizationHeader, appointmentId);
        PaymentDetails paymentDetails = paymentService.savePaymentDetails(appointment);
        String paymentDetailsAsString = paymentService.convertToString(appointment);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic12",
                "message", paymentDetailsAsString);
        kafkaProducer.send(producerRecord);
        kafkaProducer.flush();
        return ResponseEntity.ok().body(paymentDetails);
    }

}
