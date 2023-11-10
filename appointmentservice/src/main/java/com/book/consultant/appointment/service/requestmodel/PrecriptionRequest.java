package com.book.consultant.appointment.service.requestmodel;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class PrecriptionRequest {
    private String appointmentId;
    private String doctorId;
    private String userId;
    private String diagnosis;
    private List<Medicine> medicineList;
    private String emailId;
}
