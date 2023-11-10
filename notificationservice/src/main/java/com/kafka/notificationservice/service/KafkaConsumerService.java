package com.kafka.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.notificationservice.constants.GlobalConstant;
import com.kafka.notificationservice.response.Appointment;
import com.kafka.notificationservice.response.DoctorEntity;
import com.kafka.notificationservice.response.Prescription;
import com.kafka.notificationservice.response.UserEntity;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaConsumerService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SesEmailVerificationService sesEmailVerificationService;

    @KafkaListener(topics = "topic8", groupId = "group8")
    public void consumeMessage(String message) throws IOException, TemplateException {
        if(message != null){
            DoctorEntity doctor = objectMapper.readValue(message, DoctorEntity.class);
            if (GlobalConstant.ACTIVE_STATUS.equals(doctor.getStatus())) {
                sesEmailVerificationService.sendEmail(doctor);
            } else if(GlobalConstant.REJECT_STATUS.equals(doctor.getStatus())){
                sesEmailVerificationService.sendRejectionEmail(doctor);
            }
            else {
                sesEmailVerificationService.verifyEmail(doctor.getEmailId());
                //System.out.println("dooooooocccc::::  "+doctor);
            }
        }
    }

    @KafkaListener(topics = "topic9", groupId = "group9")
    public void consumeUserMessage(String message) throws IOException{
        if(message != null){
            UserEntity userEntity = objectMapper.readValue(message, UserEntity.class);
            //System.out.println(userEntity+" ::::: User ::::::");
            sesEmailVerificationService.verifyEmail(userEntity.getEmailId());
        }
    }

    @KafkaListener(topics = "topic10", groupId = "group10")
    public void consumePrescriptionMessage(String message) throws IOException, TemplateException {
        if(message != null){
            Prescription prescription= objectMapper.readValue(message, Prescription.class);
            sesEmailVerificationService.sendPrescriptionEmail(prescription);
        }
    }

    @KafkaListener(topics = "topic11", groupId = "group11")
    public void consumeAppointmentMessage(String message) throws IOException, TemplateException {
        if(message != null){
            Appointment appointment= objectMapper.readValue(message, Appointment.class);
            sesEmailVerificationService.sendAppointmentEmail(appointment);
        }
    }
}
