package es.sasensior.foodex.business.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.sasensior.foodex.business.model.CarritoCompra;
import es.sasensior.foodex.business.model.Compra;
import es.sasensior.foodex.business.model.DatosContacto;
import es.sasensior.foodex.business.model.Direccion;
import es.sasensior.foodex.business.model.EstadoCompra;
import es.sasensior.foodex.business.model.ItemCarrito;
import es.sasensior.foodex.business.model.dto.CompraDTO;
import es.sasensior.foodex.business.model.dto.UsuarioDTO;
import es.sasensior.foodex.business.services.CarritoService;
import es.sasensior.foodex.business.services.CompraService;
import es.sasensior.foodex.integration.dao.CompraPL;
import es.sasensior.foodex.integration.dao.DatosContactoPL;
import es.sasensior.foodex.integration.dao.DireccionPL;
import es.sasensior.foodex.integration.repositories.CompraRepository;
import es.sasensior.foodex.integration.repositories.ItemCarritoRepository;
import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import jakarta.transaction.Transactional;
import lombok.Getter;

@Getter
@Service
public class CompraServiceImpl implements CompraService {
	
	private final CompraRepository compraRepository;
	private final CarritoService carritoService;
	private final ItemCarritoRepository itemCarritoRepository;
	private final DozerBeanMapper mapper;
	
	public CompraServiceImpl(CompraRepository compraRepository, DozerBeanMapper mapper, CarritoService carritoService, ItemCarritoRepository itemCarritoRepository) {
		this.compraRepository = compraRepository;
		this.carritoService = carritoService;
		this.mapper = mapper;
		this.itemCarritoRepository = itemCarritoRepository;
	}

	@Override
	public List<CompraDTO> getAllComprasDTO() {
		List<CompraPL> comprasPL = compraRepository.findAll();
		
		List<Compra> comprasMapped = comprasPL.stream()
				.map(this::convertCompraPLToCompra)
				.collect(Collectors.toList());
		
		return comprasMapped.stream()
				.map(this::mapCompraToCompraDTO)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<CompraDTO> getCompraDTO(Long id) {
		Optional<CompraPL> compraPL = compraRepository.findById(id);
		if (compraPL.isPresent()) {
			Compra compra = convertCompraPLToCompra(compraPL.get());
			return Optional.of(mapCompraToCompraDTO(compra));
		} else {
			throw new IllegalStateException("No se ha encontrado ninguna compra con ese id.");
		}
	}

	@Override
	public void removeCompra(Long id) {
		Optional<CompraPL> compraPL = compraRepository.findById(id);
		if(compraPL.isEmpty()) {
			throw new IllegalStateException("No existe una compra con ese id o ya ha sido borrada."); 
		}
		compraRepository.deleteById(id);
	}
	
	@Override
	@Transactional
	public CompraDTO realizarCompra(String comentario, Direccion direccion, DatosContacto datosContacto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UsuarioPL usuarioPL = (UsuarioPL) authentication.getPrincipal();
	    
	    CompraPL compraPL = new CompraPL();
	    
	    Optional<CarritoCompra> carrito = this.carritoService.getCarrito();
	    
	    if(!(comentario.isEmpty()) || comentario != null) {
	    	compraPL.setComentario(comentario);
	    }
	    
	    compraPL.setUsuario(usuarioPL);
	    
	    if(direccion == null) {
	    	throw new IllegalStateException("Debes especificar una dirección");
	    } else {
	    	if(direccion.getCodPostal() == null || direccion.getBloque() == null || direccion.getPortal() == null || direccion.getCalle() == null) {
	    		throw new IllegalStateException("El código postal, la calle, el bloque y el portal no pueden ser nulos!");
	    	}
	    	DireccionPL direccionPL = mapper.map(direccion, DireccionPL.class);
	    	compraPL.setDireccionPL(direccionPL);
	    }
	    
	    if(datosContacto == null) {
	    	throw new IllegalStateException("Debes especificar datos de contacto.");
	    } else {
	    	if(datosContacto.getTelefono() == null) {
	    		throw new IllegalStateException("Debes especificar un número de teléfono.");
	    	}
	    	DatosContactoPL datosContactoPL = mapper.map(datosContacto, DatosContactoPL.class);
	    	compraPL.setDatosContactoPL(datosContactoPL);
	    }
	    compraPL.setFechaHora(new Date());
	    compraPL.setEstado(EstadoCompra.PAGADA);
	    
	    List<ItemCarrito> itemsCarrito = carrito.get().getItemsCarrito();
	    Double montoTotal = itemsCarrito.stream()
	            .mapToDouble(item -> item.getProducto().getPrecio() * item.getCantidad()) // Calcula el total por cada item
	            .sum();
	    compraPL.setMonto(montoTotal);

	    this.compraRepository.save(compraPL);
	    this.itemCarritoRepository.deleteAllByCarrito(carrito.get().getId());
	    
	    Compra compra = convertCompraPLToCompra(compraPL);
	    CompraDTO compraDTO = mapCompraToCompraDTO(compra);
	    
	    return compraDTO;
	    
	}


	// ********************************************
	// Métodos Privados
	// ********************************************

	// Mapea una CompraPL (base de datos) a un objeto de negocio Compra
	private Compra convertCompraPLToCompra(CompraPL compraPL) {
		Compra compra = mapper.map(compraPL, Compra.class);
		//Mapeos adicionales:
		compra.setDireccion(mapper.map(compraPL.getDireccionPL(), Direccion.class));
		compra.setDatosContacto(mapper.map(compraPL.getDatosContactoPL(), DatosContacto.class));

		return compra;
	}

	// Mapea una Compra (objeto de negocio) a un DTO de Compra
	private CompraDTO mapCompraToCompraDTO(Compra compra) {
		CompraDTO compraDTO = mapper.map(compra, CompraDTO.class);
		
		if (compra.getUsuario() != null) {
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			usuarioDTO.setApellido1(compra.getUsuario().getFirstName());
			usuarioDTO.setApellido2(compra.getUsuario().getLastName());
			usuarioDTO.setNombre(compra.getUsuario().getName());
			usuarioDTO.setDireccion(compra.getDireccion());
			usuarioDTO.setDatosContacto(compra.getDatosContacto());
			compraDTO.setUsuarioDTO(usuarioDTO);
			compraDTO.setEstadoCompra(compra.getEstado());
		}

		return compraDTO;
	}
}
