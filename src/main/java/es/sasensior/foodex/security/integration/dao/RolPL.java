package es.sasensior.foodex.security.integration.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="ROLES")
@Data
public class RolPL {
	
	@Id
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Rol name;

}
