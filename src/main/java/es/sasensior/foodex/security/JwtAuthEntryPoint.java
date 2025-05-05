package es.sasensior.foodex.security;

import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.sasensior.foodex.presentation.utils.ApiResponseBody;
import es.sasensior.foodex.presentation.utils.ResponseStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);
    
    @Autowired
    private ObjectMapper objectMapper; 

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    	
    	logger.error("Error de autenticación: {}", authException.getMessage());
    	
    	ApiResponseBody apiResponseBody = new ApiResponseBody.Builder("Se requiere autenticación para acceder a este recurso.").status(ResponseStatus.ERROR).build();
    	
    	response.setContentType("application/json");
    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    	
    	//Con esto básicamente convertimos un objeto JSON a ObjectMapper.
    	PrintWriter out = response.getWriter();
    	out.print(objectMapper.writeValueAsString(apiResponseBody));
    	out.close();
    	
    }

}