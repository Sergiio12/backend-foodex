package es.sasensior.foodex.security.integration.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "USER_ROLES")
@Data
public class UsuarioRolesPL {

	@Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USER", nullable = false, foreignKey = @ForeignKey(name = "FK_USER"))
    private UsuarioPL usuario;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ROL", nullable = false, foreignKey = @ForeignKey(name = "FK_ROL"))
    private RolPL rol;
	
}
