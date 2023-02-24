package com.cgs.authservice.controller;

import com.cgs.authservice.model.Users;
import com.cgs.authservice.model.dto.TokensEntity;
import com.cgs.authservice.model.dto.ValidationResponse;
import com.cgs.authservice.security.CurrentUser;
import com.cgs.authservice.service.TokensRedisService;
import com.cgs.authservice.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cgs.authservice.util.Constant.SUCCESS;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/authenticate/v1/")
public class AuthorizationController {

    private final TokensRedisService tokensRedisService;

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateRequestedToken(@CurrentUser Users users, HttpServletRequest request) {
        log.info("Current Login User ==> {}",users.getEmailId());
        String username = (String) request.getAttribute("username");
        String token = (String) request.getAttribute("jwt");
        Set<GrantedAuthority> grantedAuthorities = (Set<GrantedAuthority>) request.getAttribute("authorities");
        String authorities = grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return ResponseEntity.ok(ValidationResponse.builder().status("OK").methodType(HttpMethod.GET.name())
                .username(username).token(token).authorities(authorities)
                .isAuthenticated(true).build());
    }

    @GetMapping("/logout")
    public ResponseEntity<?> userLogout(@CurrentUser Users users,
                                        HttpServletRequest request) throws Exception {
        log.info("Current Login User ==> {}",users.getEmailId());
        String authToken = (String) request.getAttribute("auth-token");
        Optional<TokensEntity> byId = tokensRedisService.findById(authToken);
        if (!byId.isPresent()) {
            log.info("TokenEntity auth token not found");
            return new ResponseEntity<>(new ApiResponse(SUCCESS, "User logout failed"), HttpStatus.OK);
        }
        tokensRedisService.deleteToken(byId.get());
        return new ResponseEntity<>(new ApiResponse(SUCCESS, "User logout successfully"), HttpStatus.OK);
    }
}
