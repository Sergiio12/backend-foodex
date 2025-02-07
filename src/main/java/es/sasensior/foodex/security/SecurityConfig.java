package es.sasensior.foodex.security;

// Importaciones necesarias para configurar la seguridad en Spring Boot
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * Esta clase configura la seguridad de la aplicación utilizando Spring Security.
 */
@Configuration // Indica que esta clase proporciona configuración a la aplicación.
@EnableWebSecurity // Habilita la seguridad en la aplicación.
public class SecurityConfig {

    // Inyecta el servicio que maneja la lógica de obtención de usuarios desde la base de datos.
    @Autowired
    private UserDetailsService userDetailsService;

    // Inyecta la clase que maneja los errores de autenticación no autorizada.
    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    /**
     * Define un filtro que intercepta las peticiones y extrae el token JWT para validar la autenticación.
     */
    @Bean
    JwtAuthTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthTokenFilter();
    }

    /**
     * Configura el AuthenticationManager, que se encarga de la autenticación de usuarios.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Define el encriptador de contraseñas. Se usa BCrypt para hashear las contraseñas de los usuarios.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura un constructor para gestionar rutas con seguridad en Spring MVC.
     */
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    /**
     * Configura las reglas de seguridad para las peticiones HTTP en la aplicación.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {

        http.csrf(csrf -> csrf.disable()) // Deshabilita la protección CSRF (No recomendado en producción sin medidas adicionales).
            
            // Configura el manejo de excepciones de autenticación.
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            
            // Permite que los frames de la H2-console se carguen correctamente en la misma página.
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            
            // Define la política de sesiones como STATELESS (sin sesiones, ya que se usa JWT).
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configura las reglas de acceso a los endpoints de la aplicación.
            .authorizeHttpRequests(auth ->

                // Permite el acceso público a los endpoints de autenticación y registro.
                auth.requestMatchers("/auth/signin/**", "/auth/signup/**").permitAll()
                
                // Permite el acceso público a los recursos internos (imágenes, CSS, JS).
                .requestMatchers("/WEB-INF/**").permitAll()
                .requestMatchers("/img/**", "/css/**", "/js/**").permitAll()

                // Permite el acceso a las peticiones OPTIONS (necesarias para CORS).
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Permite el acceso público a la consola de H2 (solo en desarrollo).
                .requestMatchers("/h2-console/**").permitAll()

                // Todas las demás peticiones requieren autenticación.
                .anyRequest().authenticated()
            );

        // Configura el proveedor de autenticación que valida los usuarios en la base de datos.
        http.authenticationProvider(authenticationProvider());

        // Agrega el filtro para interceptar las peticiones y validar el token JWT antes de que lleguen al controlador.
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // *************************************************************************************
    //
    // PRIVATE METHODS
    //
    // *************************************************************************************

    /**
     * Configura el proveedor de autenticación basado en la base de datos (DAO).
     * Usa BCrypt para verificar las contraseñas almacenadas de forma segura.
     */
    private DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Usa el servicio de usuario para obtener detalles de autenticación.
        authProvider.setPasswordEncoder(passwordEncoder()); // Usa BCrypt para verificar contraseñas.

        return authProvider;
    }
}
