package com.book.consultant.appointment.service.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="appointment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Appointment {
    @JsonProperty("appointmentId")
    @Id
    private String id;
    @JsonProperty("appointmentDate")
    private String appointmentDate;
    @JsonProperty("createdDate")
    private Date createdDate;
    @JsonProperty("doctorId")
    private String doctorId;
    @JsonProperty("priorMedicalHistory")
    private String priorMedicalHistory;
    private String status;
    private String symptoms;
    @JsonProperty("timeSlot")
    private String timeSlot;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("userEmailId")
    private String userEmailId;
    @JsonProperty("userName")
    private String username;
    @JsonProperty("doctorName")
    private String doctorName;

}
