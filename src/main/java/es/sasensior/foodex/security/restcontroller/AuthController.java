package es.sasensior.foodex.security.restcontroller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

import es.sasensior.foodex.integration.dao.CarritoCompraPL;
import es.sasensior.foodex.integration.repositories.CarritoRepository;
import es.sasensior.foodex.presentation.utils.ApiResponseBody;
import es.sasensior.foodex.presentation.utils.ErrorDetail;
import es.sasensior.foodex.presentation.utils.PresentationException;
import es.sasensior.foodex.presentation.utils.ResponseStatus;
import es.sasensior.foodex.security.JwtUtils;
import es.sasensior.foodex.security.integration.dao.Rol;
import es.sasensior.foodex.security.integration.dao.RolPL;
import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import es.sasensior.foodex.security.payloads.JwtResponse;
import es.sasensior.foodex.security.payloads.LoginRequest;
import es.sasensior.foodex.security.payloads.SignupRequest;
import es.sasensior.foodex.security.services.RolPLService;
import es.sasensior.foodex.security.services.UsuarioPLService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioPLService usuarioPLService;
    
    @Autowired
    private CarritoRepository carritoRepository; 
    
    @Autowired
    private RolPLService rolPLService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch(Exception e) {
            logger.error("Error de autenticación para el usuario {}", loginRequest.getUsername());
            throw new PresentationException.Builder(HttpStatus.UNAUTHORIZED, "Credenciales inválidas.")
            .build();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UsuarioPL usuarioPL = (UsuarioPL) authentication.getPrincipal();

        List<String> roles = usuarioPL.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        usuarioPL.setFechaUltimoLogin(new Date());
        usuarioPLService.guardarEstado(usuarioPL);
        
        JwtResponse jwtResponse = new JwtResponse(jwt, usuarioPL.getId(), usuarioPL.getUsername(), usuarioPL.getEmail(), roles);
          return ResponseEntity.ok(new ApiResponseBody.Builder("Autenticación exitosa.")
        		  .status(ResponseStatus.SUCCESS)
        		  .data(jwtResponse)
        		  .build());
          
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        List<ErrorDetail> errors = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errors.add(new ErrorDetail(fieldError.getField(), fieldError.getDefaultMessage()));
            });
            
        }

        if (usuarioPLService.existsUserByEmail(signupRequest.getEmail())) {
            errors.add(new ErrorDetail("email", "Ese correo electrónico ya está registrado en nuestro sistema."));
        }

        if (usuarioPLService.existsUserByUsername(signupRequest.getUsername())) {
            errors.add(new ErrorDetail("username", "Ese nombre de usuario ya está registrado en nuestro sistema."));
        }

        if (!errors.isEmpty()) {
            throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, "Error de registro.")
                .errors(errors)
                .build();
        }

        UsuarioPL usuario = new UsuarioPL();
        usuario.setUsername(signupRequest.getUsername());
        usuario.setEmail(signupRequest.getEmail());
        usuario.setName(signupRequest.getName());
        usuario.setFirstName(signupRequest.getFirstName());
        usuario.setLastName(signupRequest.getLastName());
        
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        usuario.setPassword(encodedPassword);
        usuario.setEnabled(true);
        usuario.setFechaRegistro(new Date());
        
        Optional<RolPL> rolUsuario = rolPLService.getRol(Rol.USUARIO.toString());
        
        if (rolUsuario.isPresent()) {
            usuario.getRoles().add(rolUsuario.get());
        } else {
            throw new PresentationException.Builder(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al registrar al usuario.")
                .build();
        }

        this.usuarioPLService.register(usuario);
        
        CarritoCompraPL carritoCompra = new CarritoCompraPL();
        carritoCompra.setUsuario(usuario); 
        carritoCompra.setItemsCarrito(new ArrayList<>());
        
        this.carritoRepository.save(carritoCompra);

        return ResponseEntity.ok(new ApiResponseBody.Builder("Registro exitoso.")
            .status(ResponseStatus.SUCCESS)
            .build());
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseBody> logout(HttpServletRequest request, HttpServletResponse response) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	
    	if(auth != null) {
    		new SecurityContextLogoutHandler().logout(request, response, auth);
    	}
    	
    	return ResponseEntity.ok(new ApiResponseBody.Builder("Sesión cerrada con éxito.")
    			.status(ResponseStatus.SUCCESS)
    			.build());
    }
    
}
