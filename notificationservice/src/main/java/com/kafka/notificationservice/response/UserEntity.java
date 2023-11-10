package com.kafka.notificationservice.response;

import lombok.*;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class UserEntity {
    private String id;
    private String firstName;
    private String lastName;
    private Date dob;
    private String mobile;
    private String emailId;
    private Date createdDate;
    private Set<String> roles;
    private Set<String> fileList;
    private String password;
}
