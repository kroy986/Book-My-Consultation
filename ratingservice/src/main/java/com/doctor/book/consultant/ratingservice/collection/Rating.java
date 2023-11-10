package com.doctor.book.consultant.ratingservice.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "rating")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rating {
    @Id
    private String Id;
    private String doctorId;
    private Integer rating;
}
