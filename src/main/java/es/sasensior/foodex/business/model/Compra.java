package es.sasensior.foodex.business.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Compra {
	private Long id;
	private Cliente cliente;
	private EstadoCompra estado;
	private Date fechaHora;
	private String comentario;
	private List<ProductosCompra> productos;
}
