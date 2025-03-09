package es.sasensior.foodex.business.model;

import java.util.Date;
import java.util.List;

import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import lombok.Data;

@Data
public class Compra {
	private Long id;
	private UsuarioPL usuario;
	private EstadoCompra estado;
	private Date fechaHora;
	private String comentario;
	private List<ProductoCompra> productos;
	private Double monto;
	private Direccion direccion;
	private DatosContacto datosContacto;
}
