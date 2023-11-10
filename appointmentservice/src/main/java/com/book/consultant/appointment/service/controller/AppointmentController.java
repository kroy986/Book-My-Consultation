package com.book.consultant.appointment.service.controller;

import com.book.consultant.appointment.service.collection.*;
import com.book.consultant.appointment.service.constants.GlobalConstant;
import com.book.consultant.appointment.service.feignclientinterface.AppointmentInterface;
import com.book.consultant.appointment.service.requestmodel.AppointmentRequest;
import com.book.consultant.appointment.service.requestmodel.AvailabilityMap;
import com.book.consultant.appointment.service.requestmodel.PrecriptionRequest;
import com.book.consultant.appointment.service.responsemodel.BookingAppointmentResponse;
import com.book.consultant.appointment.service.service.AppointmentService;
import com.book.consultant.appointment.service.service.AvailabilityService;
import feign.FeignException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    AppointmentInterface appointmentInterface;
    @Autowired
    AvailabilityService availabilityService;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    KafkaProducer kafkaProducer;

    @PostMapping("/doctor/{doctorId}/availability")
    public ResponseEntity saveDoctorAvailability(@RequestBody AvailabilityMap availabilityMap,
                                                @PathVariable String doctorId,
    @RequestHeader(value = "Authorization", required = true) String authorizationHeader){

        DoctorEntity doctorEntity = appointmentInterface.getDoctorDetailsByDoctorId(authorizationHeader, doctorId);
        Availability availability = availabilityService.saveDoctorAvailability(availabilityMap, doctorId);
        return new ResponseEntity(availability, HttpStatus.OK);
    }

    @GetMapping("/doctor/{doctorId}/availability")
    public ResponseEntity getDoctorAvailability(@PathVariable String doctorId,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader) {

        DoctorEntity doctorEntity = appointmentInterface.getDoctorDetailsByDoctorId(authorizationHeader, doctorId);
        Availability availability = availabilityService.getDoctorAvailability(doctorId);
        return new ResponseEntity(availability, HttpStatus.OK);
    }

    @PostMapping("/appointments")
    public ResponseEntity bookAppointment(@RequestBody AppointmentRequest appointmentRequest,
                                        @RequestHeader(value = "Authorization", required = true) String authorizationHeader){
        DoctorEntity doctorEntity = null;
        UserEntity userEntity = appointmentInterface.getUserDetailsByUserId(authorizationHeader, appointmentRequest.getUser_id());
        Appointment appointment = appointmentService.saveAppointmentDetails(appointmentRequest, doctorEntity, userEntity);
        String appointmentEntryAsString = appointmentService.convertToString(appointment);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic11",
                "message", appointmentEntryAsString);
        kafkaProducer.send(producerRecord);
        kafkaProducer.flush();
        return ResponseEntity.ok().body(appointment.getId());
    }

    @GetMapping("/user/{userId}/appointments")
    public ResponseEntity getAppointmentDetailsByUserId(@PathVariable("userId") String userId){
            List<Appointment> appointmentDetailsByUserId = appointmentService.getAppointmentDetailsByUserId(userId);
            return ResponseEntity.ok().body(appointmentDetailsByUserId);
    }

    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<Appointment> getAppointmentByAppointmentID(@PathVariable("appointmentId") String appointmentId){
        Appointment appointment = appointmentService.getAppointmentDetailsByAppointmentId(appointmentId);
        return ResponseEntity.ok().body(appointment);
    }

    @PostMapping("/prescriptions")
    public ResponseEntity getPrescriptionByAppointmentId(@RequestBody PrecriptionRequest precriptionRequest){
        Appointment appointment = appointmentService.getAppointmentDetailsByAppointmentId(precriptionRequest.getAppointmentId());
        if(GlobalConstant.PENDING_PAYMENT.equalsIgnoreCase(appointment.getStatus())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Prescription cannot be issued since the payment status is pending");
        }
        Prescription prescription = appointmentService.savePrescriptionDetail(precriptionRequest, appointment);

        precriptionRequest = PrecriptionRequest.builder().appointmentId(prescription.getAppointmentId())
                .emailId(appointment.getUserEmailId()).userId(prescription.getUserId())
                .diagnosis(prescription.getDiagnosis()).doctorId(prescription.getDoctorId())
                .medicineList(prescription.getMedicineList()).build();

        String prescriptionEntryAsString = appointmentService.convertToString(precriptionRequest);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic10",
                "message", prescriptionEntryAsString);
        kafkaProducer.send(producerRecord);
        kafkaProducer.flush();
        return ResponseEntity.ok().body(prescription);
    }
}
