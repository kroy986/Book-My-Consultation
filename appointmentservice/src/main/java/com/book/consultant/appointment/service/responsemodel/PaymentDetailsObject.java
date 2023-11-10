package com.book.consultant.appointment.service.responsemodel;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PaymentDetailsObject {
    String id;
    String appointmentId;
    private Date createdDate;
}
