package es.sasensior.foodex.security.integration.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "USERS", uniqueConstraints = {
        @UniqueConstraint(columnNames = "USERNAME"),
        @UniqueConstraint(columnNames = "EMAIL")
})
@Data
@SequenceGenerator(name = "USUARIO_SEQ", sequenceName = "USUARIO_SEQ", allocationSize = 50)
public class UsuarioPL implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_SEQ")
    private Long id;

    @NotBlank
    @Column(length = 50, nullable = false, unique = true)
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String password;

    @NotBlank
    @Size(max = 150)
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Size(max = 50)
    @Column(length = 50)
    private String telefono;

    @NotNull
    @Column(nullable = false)
    private boolean enabled;

    @Size(min = 4, max = 50)
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Size(min = 4, max = 50)
    @Column(name = "LAST_NAME")
    private String lastName;

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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getName()))
                .collect(Collectors.toSet());
    }

}
