package com.book.consultation.user.service.controller;

import com.book.consultation.user.service.collection.UserEntity;
import com.book.consultation.user.service.service.UserService;
import jakarta.validation.Valid;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    KafkaProducer kafkaProducer;

    @PostMapping("/users")
    public ResponseEntity register(@RequestBody @Valid UserEntity userRegistrationRequest){
        UserEntity userEntity = userService.save(userRegistrationRequest);
        String userEntryAsString = userService.convertToString(userEntity);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic9", "message", userEntryAsString);
        kafkaProducer.send(producerRecord);
        kafkaProducer.flush();
        return new ResponseEntity(userEntity, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity getDoctorDetails(@PathVariable(value = "userId") String userId){
        UserEntity userEntity = userService.getUserDetailsByUserId(userId);
        return new ResponseEntity(userEntity, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/upload/document")
    public ResponseEntity uploadDocuments(@RequestParam(value = "file") MultipartFile[] files,
                                          @PathVariable(value = "userId") String userId) {

        UserEntity userEntity = userService.getUserDetailsByUserId(userId);
        String message = userService.uploadFile(files);
        userService.saveFile(userEntity);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/download/documents/{documentName}")
    public ResponseEntity downloadDocuments(@PathVariable(value = "documentName") String fileName,
                                            @PathVariable(value = "userId") String userId) {

        userService.getUserDetailsByUserId(userId);
        byte[] data = userService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping("/users/{userId}/documents/metadata")
    public ResponseEntity getDoctorVerificationFiles(@PathVariable(value = "userId") String userId){
        UserEntity userEntity = userService.getUserDetailsByUserId(userId);
        return new ResponseEntity(userEntity.getFileList(), HttpStatus.OK);
    }
}
