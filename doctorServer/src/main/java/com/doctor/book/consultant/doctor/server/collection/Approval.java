package com.doctor.book.consultant.doctor.server.collection;

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
