package com.book.consultant.appointment.service.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.book.consultant.appointment.service.collection.UserEntity;
import com.book.consultant.appointment.service.constants.RolesConstant;
import com.book.consultant.appointment.service.repository.DoctorRepo;
import com.book.consultant.appointment.service.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class UserService {

    private DoctorRepo doctorRepo;
    private UserRepo userRepo;

    private Set<String> fileList = new HashSet<>();
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${application.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private UserService(DoctorRepo doctorRepo, UserRepo userRepo){
        this.doctorRepo = doctorRepo;
        this.userRepo = userRepo;
    }

    public UserEntity getUserDetailsByUserId(String userId){
        if(userRepo.findById(userId).isPresent()){
            return userRepo.findById(userId).get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid User Id");
    }
    public UserEntity save(UserEntity userRegistrationRequest){

       if (doctorRepo.findByEmailId(userRegistrationRequest.getEmailId()).isPresent()
                || userRepo.findByEmailId(userRegistrationRequest.getEmailId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ID Already present in the Application");
        }

        Set<String> roles = new HashSet<>();
        roles.add(RolesConstant.ROLE_USER);
        roles.add(RolesConstant.ROLE_ADMIN);
        userRegistrationRequest.setRoles(roles);
        userRegistrationRequest.setCreatedDate(Calendar.getInstance().getTime());
        userRegistrationRequest.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));

        return userRepo.save(userRegistrationRequest);
    }

    public String uploadFile(MultipartFile[] files) {
        for(MultipartFile file : files){
            File fileObj = convertMultiPartFileToFile(file);
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            fileList.add(fileName);
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
            log.info(fileName+" uploaded Successfully");
            fileObj.delete();
        }
        return "Files uploaded Successfully";
    }


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    public void saveFile(UserEntity userEntity) {
        userEntity.setFileList(fileList);
        userRepo.save(userEntity);
        fileList.clear();
    }
}
