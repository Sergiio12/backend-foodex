package es.sasensior.foodex.integration.dao;

import java.util.List;

import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CARRITO_COMPRA")
public class CarritoCompraPL {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioPL usuario;
	
	@OneToMany(
	    mappedBy = "carrito", 
	    cascade = CascadeType.ALL, 
	    orphanRemoval = true,
	    fetch = FetchType.EAGER)
    private List<ItemCarritoPL> itemsCarrito;
	
}
