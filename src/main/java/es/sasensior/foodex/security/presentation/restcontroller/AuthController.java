package es.sasensior.foodex.security.presentation.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.sasensior.foodex.presentation.config.ApiResponseBody;
import es.sasensior.foodex.presentation.config.ErrorDetail;
import es.sasensior.foodex.presentation.config.PresentationException;
import es.sasensior.foodex.presentation.config.ResponseStatus;
import es.sasensior.foodex.security.JwtUtils;
import es.sasensior.foodex.security.UserDetailsImpl;
import es.sasensior.foodex.security.integration.model.UsuarioPL;
import es.sasensior.foodex.security.payloads.JwtResponse;
import es.sasensior.foodex.security.payloads.LoginRequest;
import es.sasensior.foodex.security.payloads.SignupRequest;
import es.sasensior.foodex.security.services.UsuarioPLService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioPLService usuarioPLRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = null;

        try {
            // Autentica al usuario con el nombre de usuario y contraseña proporcionados
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch(Exception e) {
            // Si la autenticación falla, registra el error y lanza una excepción con código 401 (Unauthorized)
            logger.error("Error de autenticación para el usuario {}", loginRequest.getUsername());
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseBody(ResponseStatus.ERROR, "Credenciales inválidas."));
            throw new PresentationException.Builder(HttpStatus.UNAUTHORIZED, "Credenciales inválidas.").build();
        }

        // Guarda la autenticación en el contexto de seguridad de Spring
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Genera un token JWT para el usuario autenticado
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Obtiene los detalles del usuario autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Extrae los roles del usuario y los convierte en una lista de strings
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Devuelve el token JWT y los datos del usuario autenticado en la respuesta
        JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
        return ResponseEntity.ok(new ApiResponseBody(ResponseStatus.SUCCESS, "Autenticación exitosa.", jwtResponse, null));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
    	List<ErrorDetail> errors = new ArrayList<>();

        //Lo primero, validamos que los campos estén bien.
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(fieldError -> {
            	errors.add(new ErrorDetail(fieldError.getField(), fieldError.getDefaultMessage()));
            });
        	
            //return ResponseEntity.badRequest().body(new ApiResponseBody(ResponseStatus.ERROR, "Error de validación en los campos de registro.", null, errors));
            throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, "Error de validación en los campos de registro.").errors(errors).build();
        }

        if (usuarioPLRepository.existsUserByEmail(signupRequest.getEmail())) {
        	errors.add(new ErrorDetail("email", "Ese correo electrónico ya está registrado en nuestro sistema."));
        }

        if (usuarioPLRepository.existsUserByUsername(signupRequest.getUsername())) {
        	errors.add(new ErrorDetail("username", "Ese nombre de usuario ya está registrado en nuestro sistema."));
        }

        //Si se encontraron errores con el email y el usuario, los devolvemos
        if (!errors.isEmpty()) {
              //return ResponseEntity.badRequest().body(new ApiResponseBody(ResponseStatus.ERROR, "Error de registro.", null, errors));
        	  throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, "Error de registro").errors(errors).build();
        }

        //¿Todo correcto? Pues creamos al usuario
        UsuarioPL usuario = new UsuarioPL();
        usuario.setUsername(signupRequest.getUsername());
        usuario.setEmail(signupRequest.getEmail());
        usuario.setFirstName(signupRequest.getFirstName());
        usuario.setLastName(signupRequest.getLastName());
        
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        usuario.setPassword(encodedPassword);
        usuario.setEnabled(true);

        usuarioPLRepository.register(usuario);

        return ResponseEntity.ok(new ApiResponseBody(ResponseStatus.SUCCESS, "Registro exitoso."));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseBody> logout(HttpServletRequest request, HttpServletResponse response) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	
    	if(auth != null) {
    		new SecurityContextLogoutHandler().logout(request, response, auth);
    	}
    	
    	return ResponseEntity.ok(new ApiResponseBody(ResponseStatus.SUCCESS, "Sesión cerrada con éxito."));
    	
    }
    
}
