package com.book.consultant.appointment.service.requestmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @JsonProperty("appointmentId")
    @Id
    private String id;
    @JsonProperty("appointmentDate")
    private String appointment_Date;
    @JsonProperty("createdDate")
    private Date created_Date;
    @JsonProperty("doctorId")
    private String doctor_Id;
    @JsonProperty("priorMedicalHistory")
    private String prior_Medical_History;
    private String status;
    private String symptoms;
    @JsonProperty("timeSlot")
    private String time_slot;
    @JsonProperty("userId")
    private String user_id;
    @JsonProperty("userEmailId")
    private String user_email_id;
    @JsonProperty("userName")
    private String user_name;
    @JsonProperty("doctorName")
    private String doctor_name;
}
