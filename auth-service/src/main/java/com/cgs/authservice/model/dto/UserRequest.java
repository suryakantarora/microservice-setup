package com.cgs.authservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobileNo;

    private String role;

}
