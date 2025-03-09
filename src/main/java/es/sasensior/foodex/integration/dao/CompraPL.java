package es.sasensior.foodex.integration.dao;

import java.util.Date;
import java.util.List;

import es.sasensior.foodex.business.model.EstadoCompra;
import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "COMPRAS")
@SequenceGenerator(name = "GENERAL_SEQ", sequenceName = "GENERAL_SEQ", allocationSize = 1)
public class CompraPL {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERAL_SEQ")
	private Long id;
	
	@ManyToOne //Muchas compras por un cliente.
	@JoinColumn(name = "ID_USUARIO", nullable = false, unique = false)
	private UsuarioPL usuario;
	
	@Enumerated(EnumType.STRING)
	private EstadoCompra estado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date fechaHora;
	
	private String comentario;
	
	@OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductoCompraPL> productos;
	
	private Double monto;
	
	@Embedded
	private DireccionPL direccionPL;

	@Embedded
	private DatosContactoPL datosContactoPL;
	

}
