package com.book.consultant.appointment.service.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Approval {
    private String approvedBy;
    private String approverComments;
}
