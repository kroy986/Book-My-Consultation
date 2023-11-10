package com.book.consultant.appointment.service.errorresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthErrorResponse {
    private Date timestamp;
    private Integer status;
    private String error;
    private String path;
}
