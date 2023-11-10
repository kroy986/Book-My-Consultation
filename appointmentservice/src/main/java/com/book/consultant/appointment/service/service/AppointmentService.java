package com.book.consultant.appointment.service.service;

import com.book.consultant.appointment.service.collection.Appointment;
import com.book.consultant.appointment.service.collection.DoctorEntity;
import com.book.consultant.appointment.service.collection.Prescription;
import com.book.consultant.appointment.service.collection.UserEntity;
import com.book.consultant.appointment.service.constants.GlobalConstant;
import com.book.consultant.appointment.service.repository.AppointmentRepo;
import com.book.consultant.appointment.service.repository.DoctorRepo;
import com.book.consultant.appointment.service.repository.PrescriptionRepo;
import com.book.consultant.appointment.service.requestmodel.AppointmentRequest;
import com.book.consultant.appointment.service.requestmodel.PrecriptionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    PrescriptionRepo prescriptionRepo;

    @Autowired
    ObjectMapper objectMapper;


    public Appointment saveAppointmentDetails(AppointmentRequest appointmentRequest,
                                              DoctorEntity doctorEntity,
                                              UserEntity userEntity) {
        if(doctorRepo.findById(appointmentRequest.getDoctor_Id()).isPresent()){
            doctorEntity = doctorRepo.findById(appointmentRequest.getDoctor_Id()).get();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invaild Doctor Id");
        }

        Appointment appointment = Appointment.builder()
                .doctorId(appointmentRequest.getDoctor_Id())
                .userId(appointmentRequest.getUser_id())
                .userEmailId(userEntity.getEmailId())
                .username(userEntity.getFirstName() + " " + userEntity.getLastName())
                .doctorName(doctorEntity.getFirstName() + " " + doctorEntity.getLastName())
                .appointmentDate(appointmentRequest.getAppointment_Date())
                .timeSlot(appointmentRequest.getTime_slot())
                .status(GlobalConstant.PENDING_PAYMENT)
                .createdDate(Calendar.getInstance().getTime())
                .build();

        return appointmentRepo.save(appointment);
    }

    public Appointment updateAppointmentDetails(Appointment appointment){
        return appointmentRepo.save(appointment);
    }

    public List<Appointment> getAppointmentDetailsByUserId(String userId) {
        if(appointmentRepo.findByUserId(userId).isPresent()){
            return appointmentRepo.findByUserId(userId).get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "userId not found");
    }

    public Appointment getAppointmentDetailsByAppointmentId(String appointmentId) {
        if(appointmentRepo.findById(appointmentId).isPresent()){
            return appointmentRepo.findById(appointmentId).get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment Id not found");
    }

    public Prescription savePrescriptionDetail(PrecriptionRequest precriptionRequest, Appointment appointment) {

        Prescription prescription = Prescription.builder()
                .appointmentId(precriptionRequest.getAppointmentId())
                .doctorId(precriptionRequest.getDoctorId())
                .doctorName(appointment.getDoctorName())
                .userId(precriptionRequest.getUserId())
                .diagnosis(precriptionRequest.getDiagnosis())
                .medicineList(precriptionRequest.getMedicineList())
                .build();

        Prescription prescriptionEntity = prescriptionRepo.save(prescription);

        return prescription;
    }

    public String convertToString(Object rqst) {
        try {
            return objectMapper.writeValueAsString(rqst);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
