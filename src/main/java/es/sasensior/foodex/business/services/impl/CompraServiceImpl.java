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

/**
 * Implementación del servicio para la gestión de compras.
 * Proporciona funcionalidades para realizar, consultar y eliminar compras,
 * así como para convertir entre entidades de persistencia, modelos de negocio y DTOs.
 * 
 * @Service Indica que esta clase es un componente de servicio de Spring.
 */
@Getter
@Service
public class CompraServiceImpl implements CompraService {
    
    private final CompraRepository compraRepository;
    private final CarritoService carritoService;
    private final ItemCarritoRepository itemCarritoRepository;
    private final DozerBeanMapper mapper;
    
    /**
     * Constructor para la inyección de dependencias.
     *
     * @param compraRepository Repositorio para operaciones con compras
     * @param mapper Mapeador para conversión entre entidades y modelos
     * @param carritoService Servicio para operaciones con el carrito de compra
     * @param itemCarritoRepository Repositorio para operaciones con items del carrito
     */
    public CompraServiceImpl(CompraRepository compraRepository, DozerBeanMapper mapper, 
                           CarritoService carritoService, ItemCarritoRepository itemCarritoRepository) {
        this.compraRepository = compraRepository;
        this.carritoService = carritoService;
        this.mapper = mapper;
        this.itemCarritoRepository = itemCarritoRepository;
    }

    /**
     * Obtiene todas las compras en formato DTO.
     *
     * @return Lista de todas las compras convertidas a DTO
     */
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

    /**
     * Obtiene una compra específica por su ID en formato DTO.
     *
     * @param id ID de la compra a buscar
     * @return Optional que contiene la compra en formato DTO si existe
     * @throws IllegalStateException Si no se encuentra ninguna compra con el ID especificado
     */
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

    /**
     * Elimina una compra existente.
     *
     * @param id ID de la compra a eliminar
     * @throws IllegalStateException Si no existe una compra con el ID especificado
     */
    @Override
    public void removeCompra(Long id) {
        Optional<CompraPL> compraPL = compraRepository.findById(id);
        if(compraPL.isEmpty()) {
            throw new IllegalStateException("No existe una compra con ese id o ya ha sido borrada."); 
        }
        compraRepository.deleteById(id);
    }
    
    /**
     * Realiza una nueva compra con los datos proporcionados.
     *
     * @param comentario Comentario opcional sobre la compra
     * @param direccion Dirección de entrega
     * @param datosContacto Datos de contacto para la compra
     * @return DTO con los datos de la compra realizada
     * @throws IllegalStateException Si no se especifica dirección o datos de contacto válidos
     */
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
                .mapToDouble(item -> item.getProducto().getPrecio() * item.getCantidad())
                .sum();
        compraPL.setMonto(montoTotal);

        this.compraRepository.save(compraPL);
        this.itemCarritoRepository.deleteAllByCarrito(carrito.get().getId());
        
        Compra compra = convertCompraPLToCompra(compraPL);
        CompraDTO compraDTO = mapCompraToCompraDTO(compra);
        
        return compraDTO;
    }
    
    /**
     * Obtiene todas las compras que ha realizado un usuario, dado un id por parámetro.
     * @param usuarioId es el id del usuario del que queremos obtener las compras.
     */
    @Override
    public List<CompraDTO> getComprasByUsuarioUsername(String username) {
        List<CompraPL> comprasPL = compraRepository.findByUsuarioUsername(username);
        
        return comprasPL.stream()
            .map(this::convertCompraPLToCompra)
            .map(this::mapCompraToCompraDTO)
            .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad de persistencia CompraPL a un modelo de negocio Compra.
     *
     * @param compraPL Entidad de persistencia a convertir
     * @return Modelo de negocio Compra
     */
    private Compra convertCompraPLToCompra(CompraPL compraPL) {
        Compra compra = mapper.map(compraPL, Compra.class);
        compra.setDireccion(mapper.map(compraPL.getDireccionPL(), Direccion.class));
        compra.setDatosContacto(mapper.map(compraPL.getDatosContactoPL(), DatosContacto.class));

        return compra;
    }

    /**
     * Convierte un modelo de negocio Compra a un DTO CompraDTO.
     *
     * @param compra Modelo de negocio a convertir
     * @return DTO con los datos de la compra
     */
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
