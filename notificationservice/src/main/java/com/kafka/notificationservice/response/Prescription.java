package com.kafka.notificationservice.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class Prescription {
    private String appointmentId;
    private String doctorId;
    private String userId;
    private String diagnosis;
    private List<Medicine> medicineList;
    private String emailId;
}
