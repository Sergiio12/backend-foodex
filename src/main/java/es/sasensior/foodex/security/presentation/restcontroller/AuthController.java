package es.sasensior.foodex.security.presentation.restcontroller;

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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.sasensior.foodex.presentation.config.PresentationException;
import es.sasensior.foodex.security.JwtUtils;
import es.sasensior.foodex.security.UserDetailsImpl;
import es.sasensior.foodex.security.integration.model.UsuarioPL;
import es.sasensior.foodex.security.payloads.JwtResponse;
import es.sasensior.foodex.security.payloads.LoginRequest;
import es.sasensior.foodex.security.payloads.SignupRequest;
import es.sasensior.foodex.security.services.AuthRegisterService;
import jakarta.validation.Valid;
import lombok.Getter;

// Habilita CORS para permitir peticiones desde otros dominios
@CrossOrigin
// Define que esta clase es un controlador REST
@RestController
// Define el prefijo "/auth" para todas las rutas de este controlador
@RequestMapping("/auth")
public class AuthController {

    // Logger para registrar información de ejecución
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    // Inyección del AuthenticationManager de Spring Security para autenticar usuarios
    @Autowired
    private AuthenticationManager authenticationManager;

    // Servicio para gestionar el registro de usuarios
    @Autowired
    @Getter
    private AuthRegisterService authRegisterService;

    // Clase de utilidad para gestionar JWT
    @Autowired
    private JwtUtils jwtUtils;

    // Endpoint para iniciar sesión ("/auth/signin")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = null;

        try {
            // Autentica al usuario con el nombre de usuario y contraseña proporcionados
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch(Exception e) {
            // Si la autenticación falla, registra el error y lanza una excepción con código 401 (Unauthorized)
            logger.error("Error de autenticación para el usuario {}", loginRequest.getUsername());
            throw new PresentationException("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
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
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    // Endpoint para registrar un nuevo usuario ("/auth/signup")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {

        // Verifica si el email ya está registrado en la base de datos
        if(authRegisterService.existsUserByEmail(signupRequest.getEmail())) {
            throw new PresentationException("El correo electrónico ya está registrado.", HttpStatus.BAD_REQUEST);
        }

        // Verifica si el nombre de usuario ya está registrado en la base de datos
        if(authRegisterService.existsUserByUsername(signupRequest.getUsername())) {
            throw new PresentationException("El nombre de usuario ya está registrado.", HttpStatus.BAD_REQUEST);
        }

        // Crea una nueva instancia de usuario
        UsuarioPL usuario = new UsuarioPL();
        usuario.setUsername(signupRequest.getUsername());
        usuario.setEmail(signupRequest.getEmail());
        usuario.setFirstName(signupRequest.getFirstName());
        usuario.setLastName(signupRequest.getLastName());
        usuario.setPassword(signupRequest.getPassword()); // **OJO:** Asegúrate de encriptar la contraseña antes de guardarla.
        usuario.setEnabled(true);

        // Guarda el usuario en la base de datos
        authRegisterService.guardarUsuarioBBDD(usuario);

        // Retorna una respuesta indicando que el usuario fue creado exitosamente
        return ResponseEntity.ok("Usuario creado con éxito.");
    }

}
