package com.cgs.authservice.security;

import com.cgs.authservice.util.ApiResponse;
import com.cgs.authservice.util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @author kvinothkumar
 * @version 1.0
 * @apiNote user permission denied handler
 * @createdAt 04-12-2021
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final HttpMessageConverter<String> messageConverter;

    private final ObjectMapper mapper;

    public CustomAccessDeniedHandler(ObjectMapper mapper) {
        this.messageConverter = new StringHttpMessageConverter();
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException {
        ApiResponse apiError = new ApiResponse();
        apiError.setMessage("user don't have any right to access this resource");
        apiError.setData(e.getMessage());
        apiError.setRespCode(Constant.UNAUTHORIZED);
        ServerHttpResponse outputMessage = new ServletServerHttpResponse(httpServletResponse);
        outputMessage.setStatusCode(HttpStatus.BAD_REQUEST);

        messageConverter.write(mapper.writeValueAsString(apiError),
                MediaType.APPLICATION_JSON, outputMessage);
    }
}
