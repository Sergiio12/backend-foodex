package es.sasensior.foodex.security.presentation.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
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

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Getter
    private AuthRegisterService authRegisterService;

    @Autowired
    private JwtUtils jwtUtils;

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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }

        // Lógica de negocio: verificación de la existencia de usuario o correo
        if (authRegisterService.existsUserByEmail(signupRequest.getEmail())) {
            errors.put("email", "El correo electrónico ya está registrado.");
        }

        if (authRegisterService.existsUserByUsername(signupRequest.getUsername())) {
            errors.put("username", "El nombre de usuario ya está registrado.");
        }

        // Si se encontraron errores, los devolvemos
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        // Crear el usuario
        UsuarioPL usuario = new UsuarioPL();
        usuario.setUsername(signupRequest.getUsername());
        usuario.setEmail(signupRequest.getEmail());
        usuario.setFirstName(signupRequest.getFirstName());
        usuario.setLastName(signupRequest.getLastName());
        
        String encodedPassword = new BCryptPasswordEncoder().encode(signupRequest.getPassword());
        usuario.setPassword(encodedPassword);

        usuario.setEnabled(true);

        authRegisterService.guardarUsuarioBBDD(usuario);

        return ResponseEntity.ok("Usuario creado con éxito.");
    }

}
