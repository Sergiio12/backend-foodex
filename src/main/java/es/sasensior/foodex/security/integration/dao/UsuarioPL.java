package es.sasensior.foodex.security.integration.dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "USERS", uniqueConstraints = {
        @UniqueConstraint(columnNames = "USERNAME"),
        @UniqueConstraint(columnNames = "EMAIL")
})
@Data
@SequenceGenerator(name = "USERS_SEQ", sequenceName = "USERS_SEQ", allocationSize = 1)
public class UsuarioPL implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    @Column(length = 100, nullable = false)
    @JsonIgnore
    private String password;
    
    @NotBlank
    @Email
    private String email;

    private String telefono;

    @NotNull
    private boolean enabled;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
    
    @NotBlank
    private String name;
    
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ULTIMO_LOGIN")
    private Date fechaUltimoLogin;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "ID_USER"),
            inverseJoinColumns = @JoinColumn(name = "ID_ROL")
    )
    private Set<RolPL> roles = new HashSet<>();

    /**
     * Devuelve los roles del usuario como `GrantedAuthority` para Spring Security.
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getName()))
                .collect(Collectors.toSet());
    }
    
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
    	return UserDetails.super.isAccountNonExpired();
    }
    
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
    	return UserDetails.super.isAccountNonLocked();
    }
    
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
    	return UserDetails.super.isCredentialsNonExpired();
    }

}
