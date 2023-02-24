package com.cgs.authservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class TokenGeneration {

    private String key = "sib_secret_string_to_validate";

//    public String generateToken(LoginBean loginBean) {
//        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
//        Instant expiration = issuedAt.plus(60, ChronoUnit.MINUTES);
//        return Jwts.builder()
//                .setSubject(loginBean.getEmailid()) // username
//                .claim("AUTHORITIES_KEY", loginBean.getFirstname()) // type of user
//                .signWith(SignatureAlgorithm.HS512, key)
//                .setIssuedAt(Date.from(issuedAt))
//                .setExpiration(Date.from(expiration))
//                .setId(loginBean.getUserid()) // userId
//                .compact();
//    }

}