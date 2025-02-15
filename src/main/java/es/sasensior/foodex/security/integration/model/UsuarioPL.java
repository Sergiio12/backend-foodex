package es.sasensior.foodex.security.integration.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name="USERS", uniqueConstraints = {
		@UniqueConstraint(columnNames = "USERNAME"),
		@UniqueConstraint(columnNames = "EMAIL")
})
@Data
public class UsuarioPL {
	
	@Id
	@GeneratedValue(generator = "USUARIO_SEQ")
	private Long id;
	
	@NotBlank
	@Column(length=50)
	@Size(min=4, max=50)
	private String username;
	
	private String password;
	
	@NotBlank
	@Size(max=150)
	@Email
	private String email;
	
	@Column(length=50)
    @Size(min = 4, max = 50)
	private String telefono;
	
	@NotNull
	private boolean enabled;
	
	@Size(min=4, max=50)
    @Column(name = "FIRST_NAME")
	private String firstName;
	
	@Size(min=4, max=50)
    @Column(name = "LAST_NAME")
	private String lastName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_PASSWORD_RESET_DATE")
	private Date lastPasswordResetDate;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		    name = "USER_ROLES", 
		    joinColumns = @JoinColumn(name = "ID_USER"), 
		    inverseJoinColumns = @JoinColumn(name = "ID_ROL")
		)
	private Set<RolPL> roles = new HashSet<>(); 

}
