package com.doctor.book.consultant.doctor.server.controller;

import com.doctor.book.consultant.doctor.server.collection.Approval;
import com.doctor.book.consultant.doctor.server.collection.DoctorEntity;
import com.doctor.book.consultant.doctor.server.constants.GlobalConstant;
import com.doctor.book.consultant.doctor.server.service.DoctorService;
import jakarta.validation.Valid;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class DoctorController {
    @Autowired
    DoctorService doctorService;

    @Autowired
    KafkaProducer kafkaProducer;

    @PostMapping("/doctors/register")
    public ResponseEntity register(@RequestBody @Valid DoctorEntity doctorRegistrationRequest) {
        DoctorEntity doctorEntity = doctorService.save(doctorRegistrationRequest);
        String doctorEntryAsString = doctorService.convertToString(doctorEntity);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic8", "message", doctorEntryAsString);
        kafkaProducer.send(producerRecord);
        kafkaProducer.flush();
       // kafkaProducer.close();
        return new ResponseEntity(doctorEntity, HttpStatus.CREATED);
    }

    @PostMapping("/doctors/{doctorId}/upload/document")
    public ResponseEntity uploadDocuments(@RequestParam(value = "file") MultipartFile[] files,
                                          @PathVariable(value = "doctorId") String doctorId) {

        DoctorEntity doctorEntity = doctorService.getDoctorDetailsByDoctorId(doctorId);
        String message = doctorService.uploadFile(files);
        doctorService.saveFile(doctorEntity);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        return new ResponseEntity(message, multiValueMap, HttpStatus.OK);
    }

    @GetMapping("/doctors/{doctorId}/download/documents/{documentName}")
    public ResponseEntity downloadDocuments(@PathVariable(value = "documentName") String fileName,
                                            @PathVariable(value = "doctorId") String doctorId) {

        doctorService.getDoctorDetailsByDoctorId(doctorId);
        byte[] data = doctorService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .accepted()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @PutMapping("/doctors/{doctorId}/approve")
    public ResponseEntity doctorApproveStatusUpdate(@PathVariable(value = "doctorId") String doctorId,
                                             @RequestBody Approval approval){
            String status = GlobalConstant.ACTIVE_STATUS;
            DoctorEntity doctorEntity = doctorService.changeApprovalStatus(approval, doctorId, status);
            String doctorEntryAsString = doctorService.convertToString(doctorEntity);
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic8", "message", doctorEntryAsString);
            kafkaProducer.send(producerRecord);
            kafkaProducer.flush();
            return new ResponseEntity(doctorEntity, HttpStatus.OK);
    }

    @PutMapping("/doctors/{doctorId}/reject")
    public ResponseEntity doctorRejectStatusUpdate(@PathVariable(value = "doctorId") String doctorId,
                                             @RequestBody Approval approval){
        String status = GlobalConstant.REJECT_STATUS;
        DoctorEntity doctorEntity = doctorService.changeApprovalStatus(approval, doctorId, status);
        String doctorEntryAsString = doctorService.convertToString(doctorEntity);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic8", "message", doctorEntryAsString);
        kafkaProducer.send(producerRecord);
        kafkaProducer.flush();
        return new ResponseEntity(doctorEntity, HttpStatus.OK);
    }

    @GetMapping("/doctors")
    public ResponseEntity getDoctorDetailsBasedOnSpecialityAndStatus(@RequestParam(value = "speciality", required = false) String speciality,
                                                                     @RequestParam(value = "status") String status) {
        if (status != null && speciality == null) {
            Page<DoctorEntity> doctorEntity = doctorService
                    .getDoctorsBasedOnStatusAndSortByRating(status, 20);
            return new ResponseEntity(doctorEntity.get(), HttpStatus.OK);
        } else if (status != null && speciality != null) {
            Page<DoctorEntity> doctorEntity = doctorService
                    .getDoctorsBasedOnStatusAndSpecialityAndSortByRating(status, speciality, 20);
            return new ResponseEntity(doctorEntity.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity("", HttpStatus.OK);
        }
    }

    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity getDoctorDetails(@PathVariable(value = "doctorId") String doctorId){
        DoctorEntity doctorEntity = doctorService.getDoctorDetailsByDoctorId(doctorId);
        return new ResponseEntity(doctorEntity, HttpStatus.OK);
    }

    @GetMapping("/doctors/{doctorId}/documents/metadata")
    public ResponseEntity getDoctorVerificationFiles(@PathVariable(value = "doctorId") String doctorId){
        DoctorEntity doctorEntity = doctorService.getDoctorDetailsByDoctorId(doctorId);
        return new ResponseEntity(doctorEntity.getFileList(), HttpStatus.OK);
    }
}
