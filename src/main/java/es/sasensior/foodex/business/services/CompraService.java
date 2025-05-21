package es.sasensior.foodex.business.services;

import java.util.List;
import java.util.Optional;

import es.sasensior.foodex.business.model.DatosContacto;
import es.sasensior.foodex.business.model.Direccion;
import es.sasensior.foodex.business.model.dto.CompraDTO;

public interface CompraService {
	
	List<CompraDTO> getAllComprasDTO();
	
	Optional<CompraDTO> getCompraDTO(Long id);
	
	void removeCompra(Long id);
	
	CompraDTO realizarCompra(String comentario, Direccion direccion, DatosContacto datosContacto);

	List<CompraDTO> getComprasByUsuarioUsername(String username);

}
