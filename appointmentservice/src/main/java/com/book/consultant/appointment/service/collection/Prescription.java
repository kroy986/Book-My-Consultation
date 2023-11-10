package com.book.consultant.appointment.service.collection;

import com.book.consultant.appointment.service.requestmodel.Medicine;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "prescription")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Prescription {
    @Id
    private String id;
    private String userId;
    private String doctorId;
    private String doctorName;
    private String appointmentId;
    private String diagnosis;
    private List<Medicine> medicineList;
}
