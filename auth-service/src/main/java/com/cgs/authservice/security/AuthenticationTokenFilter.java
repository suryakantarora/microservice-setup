package com.cgs.authservice.security;


import com.cgs.authservice.model.dto.TokensEntity;
import com.cgs.authservice.service.TokensRedisService;
import com.cgs.authservice.util.ApiResponse;
import com.cgs.authservice.util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author kvinothkumar
 * @version 1.0
 * @apiNote Authentication Token Filter
 * @createdAt 04-12-2021
 */
public class AuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public AuthenticationTokenFilter() {
        super("/api/**");
    }

    @Autowired
    private TokensRedisService tokensRedisService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        final HttpMessageConverter<String> messageConverter = new StringHttpMessageConverter();
        final ObjectMapper mapper = new ObjectMapper();
        String header = request.getHeader("Authorization");
        logger.info("METHOD : " + request.getMethod() + " " + request.getMethod().equals(HttpMethod.OPTIONS.name()));
        if (CorsUtils.isPreFlightRequest(request)) {
            response.setStatus(HttpServletResponse.SC_OK);
            System.out.println("METHOD : " + request.getMethod() + " " + request.getMethod().equals(HttpMethod.OPTIONS.name()));
            return null;
        }
        if (header == null || header.length() <= 6 || !header.startsWith("Token ")) {
            ApiResponse apiError = new ApiResponse();
            apiError.setMessage("token not found in the header");
            apiError.setRespCode(Constant.UNAUTHORIZED);
            ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
            outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);
            System.out.println(apiError);
            messageConverter.write(mapper.writeValueAsString(apiError),
                    MediaType.APPLICATION_JSON, outputMessage);
            return null;
        }
        final String authToken = header.substring(6);
        Optional<TokensEntity> tokensEntity = tokensRedisService.findById(authToken);
        if (!tokensEntity.isPresent()) {
            ApiResponse apiError = new ApiResponse();
            apiError.setMessage("Requested token not valid, please try again with new one.");
            apiError.setRespCode(Constant.UNAUTHORIZED);
            ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
            outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);
            System.out.println(apiError);
            messageConverter.write(mapper.writeValueAsString(apiError),
                    MediaType.APPLICATION_JSON, outputMessage);
            return null;
        }
        String username = null;
        String authenticationToken = null;
        try {
            authenticationToken = tokensEntity.get().getAuthenticationToken();
            username = jwtTokenUtil.getUsernameFromToken(authenticationToken);
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT Token has expired");
            ApiResponse apiError = new ApiResponse();
            apiError.setMessage("JWT Token has expired");
            apiError.setRespCode(Constant.UNAUTHORIZED);
            ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
            outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);
            System.out.println(apiError);
            messageConverter.write(mapper.writeValueAsString(apiError),
                    MediaType.APPLICATION_JSON, outputMessage);
            return null;
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);


            // if token is valid configure Spring Security to manually set
            // authentication
            if (jwtTokenUtil.validateToken(authenticationToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                request.setAttribute("username", username);
                request.setAttribute("authorities", userDetails.getAuthorities());
                request.setAttribute("jwt", authenticationToken);
                request.setAttribute("auth-token",authToken);
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }

}
