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

import es.sasensior.foodex.presentation.config.HttpErrorCustomizado;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
	
	//Esta clase se usa para cuando usuario quiere acceder a un recurso restringido, se le lanza un error.

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);
    
    @Autowired
    private ObjectMapper objectMapper; //Convierte y lee objetos JSON.

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    	
    	logger.error("Unauthorized error: {}", authException.getMessage());
    
    	HttpErrorCustomizado httpErrorCustomizado = new HttpErrorCustomizado(authException.getMessage());
    	
    	response.setContentType("application/json"); //Indica que la respuesta será en formato JSON.
    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //Establece el código de error 401 (Unauthorized).
    	
    	//Con esto básicamente convertimos un objeto JSON a ObjectMapper.
    	PrintWriter out = response.getWriter();
    	out.print(objectMapper.writeValueAsString(httpErrorCustomizado));
    	out.close();
    	
    }

}