package com.cgs.authservice.controller;


import com.cgs.authservice.model.Users;
import com.cgs.authservice.model.dto.LoginRequestDto;
import com.cgs.authservice.model.dto.TokensEntity;
import com.cgs.authservice.model.dto.UserRequest;
import com.cgs.authservice.repository.UserRepo;
import com.cgs.authservice.security.JwtTokenUtil;
import com.cgs.authservice.service.TokensRedisService;
import com.cgs.authservice.service.UserServices;
import com.cgs.authservice.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import static com.cgs.authservice.util.Constant.SUCCESS;


/**
 * @author kvinothkumar
 * @version 1.0
 * @apiNote User Details Crud operation
 * @since 1.8
 */
@RestController
@RequestMapping(value = "/authenticate")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepo userRepo;
    private final UserServices userServices;
    private final PasswordEncoder passwordEncoder;
    private final TokensRedisService tokensRedisService;

    @PostMapping("/user")
    public ResponseEntity<?> userAuthenticate(@RequestBody LoginRequestDto data, HttpServletRequest request) throws Exception {
        log.info("Request Data: ==>{}", data);
        Users user = userServices.findUser(data);
        HashMap<String, Object> result = new HashMap<>();
        if (user != null && passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            final Authentication authentication = authenticate(
                    data.getUserName(),
                    data.getPassword()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String jwtToken = jwtTokenUtil.generateToken(user);
            log.info("Microservice Token ==> {}", jwtToken);
            /*Todo Storing the token using redis database to avoid expose jwt token outside*/
            TokensEntity tokensEntity = TokensEntity.builder().id(generateUuid()).authenticationToken(jwtToken)
                    .username(user.getUsername())
                    .createdBy("SYSTEM").createdOn(LocalDateTime.now())
                    .modifiedBy("SYSTEM").modifiedOn(LocalDateTime.now())
                    .build();
            tokensEntity = tokensRedisService.save(tokensEntity);
            result.put("token", String.format("Token %s", tokensEntity.getId()));
        }
        return new ResponseEntity<>(new ApiResponse(SUCCESS, "Success", result), HttpStatus.OK);
    }

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    @PostMapping("/create")
    public ResponseEntity<?> userAuthenticate(@RequestBody UserRequest data, HttpServletRequest request) throws Exception {
        /*Todo need to append validation*/
        Users users = new Users();
        users.setUserName(data.getUserName());
        users.setUserPassword(passwordEncoder.encode(data.getPassword()));
        users.setEmailId(data.getEmailId());
        users.setFirstName(data.getFirstName());
        users.setLastName(data.getLastName());
        users.setMobileNo(data.getMobileNo());
        users.setRole(data.getRole());
        Users save = userRepo.save(users);
        return new ResponseEntity<>(new ApiResponse(SUCCESS, "Success", save), HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
