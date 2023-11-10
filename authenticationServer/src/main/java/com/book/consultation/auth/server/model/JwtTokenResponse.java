package com.book.consultation.auth.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtTokenResponse {
    private String accessToken;
    private String tokenType;
}
