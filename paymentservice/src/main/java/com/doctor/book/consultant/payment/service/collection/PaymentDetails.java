package com.doctor.book.consultant.payment.service.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "paymentdetails")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDetails {
    @Id
    String id;
    String appointmentId;
    private Date createdDate;
}
