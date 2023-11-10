package com.doctor.book.consultant.payment.service.collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;
    @NotNull(message = "FirstName")
    @Size(max = 20, message = "FirstName cannot exceed 20 characters")
    private String firstName;
    @NotNull(message = "LastName")
    @Size(max = 20, message = "LastName cannot exceed 20 characters")
    private String lastName;
    @NotNull(message = "Date Of Birth")
    private Date dob;
    @NotNull(message = "Mobile Number")
    @Size(max = 10, message = "Mobile cannot exceed 10 numbers")
    private String mobile;
    @NotNull(message = "Email")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    @JsonProperty("emailId")
    @Indexed(unique = true)
    private String emailId;
    private Date createdDate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<String> roles;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<String> fileList;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password")
    private String password;
}
