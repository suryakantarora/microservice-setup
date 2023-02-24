package com.cgs.gateway.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class ValidationResponse {
    private String status;
    private boolean isAuthenticated;
    private String methodType;
    private String username;
    private String token;
    private String authorities;

}
