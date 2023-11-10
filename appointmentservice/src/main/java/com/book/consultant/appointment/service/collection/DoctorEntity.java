package com.book.consultant.appointment.service.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(collection = "doctors")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorEntity {
    @Id
    private String id;
    @NotNull(message = "FirstName cannot be Empty")
    @Size(max = 20, message = "FirstName cannot exceed 20 characters")
    private String firstName;
    @NotNull(message = "LastName cannot be Empty")
    @Size(max = 20, message = "LastName cannot exceed 20 characters")
    private String lastName;
    private String speciality = "GENERAL_PHYSICIAN";
    @NotNull(message = "Date Of Birth cannot be Empty")
    private Date dob;
    @NotNull(message = "Mobile Number cannot be Empty")
    @Size(max = 10, message = "Mobile cannot exceed 10 numbers")
    private String mobile;
    @NotNull(message = "Email cannot be Empty")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    @JsonProperty("emailId")
    @Indexed(unique = true)
    private String emailId;
    @NotNull(message = "PAN card number cannot be Empty")
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN card format")
    private String pan;
    private String status;
    private String approvedBy;
    private String approverComments;
    private Date registrationDate = new Date();
    private Date verificationDate;
    @NotNull(message = "Password cannot be Empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<String> roles;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<String> fileList;
    private Integer rating;
}
