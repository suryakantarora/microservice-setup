package com.cgs.authservice.service;

import com.cgs.authservice.model.Users;
import com.cgs.authservice.model.dto.LoginRequestDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserServices extends UserDetailsService {

    Users findUser(LoginRequestDto data);
}
