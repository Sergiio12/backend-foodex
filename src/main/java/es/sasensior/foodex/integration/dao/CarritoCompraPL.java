package es.sasensior.foodex.integration.dao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CARRITO_COMPRA")
@SequenceGenerator(name = "GENERAL_SEQ", sequenceName = "GENERAL_SEQ", allocationSize = 1)
public class CarritoCompraPL {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERAL_SEQ")
	@JsonIgnore
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	@JsonIgnore
	private UsuarioPL usuario;
	
	@OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemCarritoPL> itemsCarrito;
	
}
