package es.sasensior.foodex.integration.dao;

import java.util.Date;
import java.util.List;

import es.sasensior.foodex.business.model.EstadoCompra;
import jakarta.persistence.CascadeType;
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
	@JoinColumn(name = "ID_CLIENTE", nullable = false, unique = false)
	private ClientePL cliente;
	
	@Enumerated(EnumType.STRING)
	private EstadoCompra estado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date fechaHora;
	
	private String comentario;
	
	@OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductoCompraPL> productos;
	

}
