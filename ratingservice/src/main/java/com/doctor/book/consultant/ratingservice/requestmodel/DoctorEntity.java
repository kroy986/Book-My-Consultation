package com.doctor.book.consultant.ratingservice.requestmodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class DoctorEntity {
    @Id
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
    private Date registrationDate ;
    private Date verificationDate;
    private String password;
    private Set<String> roles;
    private Set<String> fileList;
    private Integer rating;
}
