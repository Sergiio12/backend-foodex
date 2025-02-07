package es.sasensior.foodex.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthTokenFilter extends OncePerRequestFilter { 
	
	/**
	 * Este filtro se ejecuta antes de procesar cada petición, revisando si hay un token JWT válido en los headers.
	 * Solamente se ejecuta una vez por petición (De ahí el que implemente 'OncePerRequestFilter').
	 */

	 @Autowired
	 private JwtUtils jwtUtils; //Esto se utiliza para extraer información del token y validarla.
	 
	 @Autowired
	 private UserDetailsServiceImpl userDetailsService; //Con esto cargamos los datos del usuario en la base de datos.
	 
	 //Cada vez que hay una petición, spring ejecuta esto.
	 @Override
	 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		 
		 try {
			 
			 String jwt = parseJwt(request); //Obtiene el token de la cabecera de autentificación.
			 
			 if (jwt != null && jwtUtils.validateJwtToken(jwt)) { //El token existe y es valido??
				 
				 String username = jwtUtils.getUserNameFromJwtToken(jwt); //¿A qué usuario pertenece ese token?
				 UserDetails userDetails = userDetailsService.loadUserByUsername(username); //Cargamos datos del usuario.
				 
				 UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	             authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //Añade los detalles de la solicitud.
	             
	             SecurityContextHolder.getContext().setAuthentication(authentication); //Guarda la autenticación en el contexto de seguridad.
			 }
		 
		 } catch (Exception e) { //¿Es el token inválido o ha expirado?
			 
			 logger.error("Cannot set user authentication: ", e);
		 }
		 
		 filterChain.doFilter(request, response);
		 
	 }

	 // *************************************************************************************
	 //
	 // PRIVATE METHODS
	 //
	 // *************************************************************************************

	 private String parseJwt(HttpServletRequest request) { //Esto es para buscar el token en la cabecera
	    	
		String headerAuth = request.getHeader("Authorization"); 
	        
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			 return headerAuth.substring(7);
		}
		
		return null;
	}
}