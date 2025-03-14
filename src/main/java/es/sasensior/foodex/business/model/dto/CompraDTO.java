package es.sasensior.foodex.business.model.dto;

import java.util.Date;

import es.sasensior.foodex.business.model.EstadoCompra;
import lombok.Data;

@Data
public class CompraDTO {
	private Long id;
	private UsuarioDTO usuarioDTO;
	private EstadoCompra estadoCompra;
	private Date fechaHora;
	private Double monto;
}
