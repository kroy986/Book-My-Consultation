package com.kafka.notificationservice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class DoctorEntity {
    private String id;
    private String firstName;
    private String lastName;
    private String speciality;
    private Date dob;
    private String mobile;
    private String emailId;
    private String pan;
    private String status;
    private String approvedBy;
    private String approverComments;
    private Date registrationDate;
    private Date verificationDate;
    private String password;
    private Set<String> roles;
    private Set<String> fileList;
    private Integer rating;
}
