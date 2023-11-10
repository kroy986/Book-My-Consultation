package com.doctor.book.consultant.doctor.server.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.doctor.book.consultant.doctor.server.collection.Approval;
import com.doctor.book.consultant.doctor.server.collection.DoctorEntity;
import com.doctor.book.consultant.doctor.server.constants.GlobalConstant;
import com.doctor.book.consultant.doctor.server.constants.RolesConstant;
import com.doctor.book.consultant.doctor.server.repository.DoctorRepo;
import com.doctor.book.consultant.doctor.server.repository.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class DoctorService{

    private final DoctorRepo doctorRepo;
    private final UserRepo userRepo;

    private Set<String> fileList = new HashSet<>();
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${application.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private DoctorService(DoctorRepo doctorRepo, UserRepo userRepo){
        this.doctorRepo = doctorRepo;
        this.userRepo = userRepo;
    }

    public DoctorEntity getDoctorDetailsByDoctorId(String doctorId){
        if(doctorRepo.findById(doctorId).isPresent()){
            return doctorRepo.findById(doctorId).get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Doctor Id");
    }
    public DoctorEntity save(DoctorEntity doctorRegistrationRequest){

       if (doctorRepo.findByEmailId(doctorRegistrationRequest.getEmailId()).isPresent()
                || userRepo.findByEmailId(doctorRegistrationRequest.getEmailId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ID Already present in the Application");
        }

        doctorRegistrationRequest.setStatus(GlobalConstant.PENDING_STATUS);

        Set<String> roles = new HashSet<>();
        roles.add(RolesConstant.ROLE_USER);
        //roles.add(RolesConstant.ROLE_ADMIN);
        doctorRegistrationRequest.setRoles(roles);

        doctorRegistrationRequest.setPassword(passwordEncoder.encode(doctorRegistrationRequest.getPassword()));

        return doctorRepo.save(doctorRegistrationRequest);
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

    public void saveFile(DoctorEntity doctorEntity) {
        doctorEntity.setFileList(fileList);
        doctorRepo.save(doctorEntity);
        fileList.clear();
    }

    @Cacheable("doctorDetailCache")
    public Page<DoctorEntity> getDoctorsBasedOnStatusAndSortByRating(String status, int limit) {
        Sort sort = Sort.by(Sort.Direction.DESC, "rating");
        PageRequest pageRequest = PageRequest.of(0, limit, sort);
        Page<DoctorEntity> doctorEntities = doctorRepo.findByStatus(status, pageRequest);
        return doctorEntities;
    }

    public Page<DoctorEntity> getDoctorsBasedOnSpecialityAndSortByRating(String speciality, int limit) {
        Sort sort = Sort.by(Sort.Direction.DESC, "rating");
        PageRequest pageRequest = PageRequest.of(0, limit, sort);
        Page<DoctorEntity> doctorEntities = doctorRepo.findBySpeciality(speciality, pageRequest);
        return doctorEntities;
    }

    @Cacheable("doctorDetailCacheOnStatusAndSpeciality")
    public Page<DoctorEntity> getDoctorsBasedOnStatusAndSpecialityAndSortByRating(String status, String speciality, int limit) {
        Sort sort = Sort.by(Sort.Direction.DESC, "rating");
        PageRequest pageRequest = PageRequest.of(0, limit, sort);
        Page<DoctorEntity> doctorEntities = doctorRepo.findByStatusAndSpeciality(status, speciality, pageRequest);
        return doctorEntities;
    }

    public DoctorEntity changeApprovalStatus(@NotNull Approval approval, String doctorId, String status){
       DoctorEntity doctor =  getDoctorDetailsByDoctorId(doctorId);
       doctor.setApprovedBy(approval.getApprovedBy());
       doctor.setApproverComments(approval.getApproverComments());
       doctor.setStatus(status);
       return doctorRepo.save(doctor);
    }

    public String convertToString(Object doctorEntity) {
        try {
            return objectMapper.writeValueAsString(doctorEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
