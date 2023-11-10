package com.doctor.book.consultant.doctor.server.responsemodel;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Rating {
    private String Id;
    private String doctorId;
    private Integer rating;
}
