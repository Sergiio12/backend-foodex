package es.sasensior.foodex.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore; // Evita que la contraseña se serialice en JSON.

import es.sasensior.foodex.security.integration.model.UsuarioPL;

/**
 * Implementación personalizada de UserDetails.
 * 
 * Esta clase representa a un usuario autenticado en Spring Security y
 * almacena la información relevante para la autenticación y autorización.
 */
public class UserDetailsImpl implements UserDetails {
    
    private static final long serialVersionUID = 1L; // Versión para la serialización.

    private Long id;
    private String username;
    
    @JsonIgnore // Evita que la contraseña se incluya en las respuestas JSON.
    private String password;
    
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor privado para la creación de instancias de UserDetailsImpl.
     */
    private UserDetailsImpl(Long id, String username, String password, String firstName, String lastName, String email, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    /**
     * Método estático para construir un objeto UserDetailsImpl a partir de un UsuarioPL (usuario en BD).
     * Convierte la lista de roles del usuario en una lista de GrantedAuthority, que es lo que Spring Security usa.
     */
    public static UserDetailsImpl build(UsuarioPL usuarioPL) {
        
        List<GrantedAuthority> roles = usuarioPL.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())) // Convierte cada rol en una autoridad de Spring.
                .collect(Collectors.toList());

        return new UserDetailsImpl(usuarioPL.getId(), 
                                   usuarioPL.getUsername(),
                                   usuarioPL.getPassword(),
                                   usuarioPL.getFirstName(),
                                   usuarioPL.getLastName(),
                                   usuarioPL.getEmail(),
                                   usuarioPL.isEnabled(),
                                   roles);
    }

    // Métodos de la interfaz UserDetails

    /**
     * Devuelve la lista de roles (authorities) del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Devuelve la contraseña del usuario.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Devuelve el nombre de usuario.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Devuelve si el usuario está habilitado o no.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Métodos adicionales para obtener información del usuario

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
