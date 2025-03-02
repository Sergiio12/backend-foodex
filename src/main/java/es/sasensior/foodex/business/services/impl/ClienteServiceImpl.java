package es.sasensior.foodex.business.services.impl;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import es.sasensior.foodex.business.model.Cliente;
import es.sasensior.foodex.business.services.ClienteService;
import es.sasensior.foodex.integration.dao.ClientePL;
import es.sasensior.foodex.integration.repositories.ClienteRepository;
import lombok.Getter;

@Service
@Getter //Para quitar la advertencia de 'valor no usado'.
public class ClienteServiceImpl implements ClienteService {
	
	private final ClienteRepository clienteRepository;
	private DozerBeanMapper mapper;
	
	public ClienteServiceImpl(ClienteRepository clienteRepository, DozerBeanMapper mapper) {
		this.clienteRepository = clienteRepository;
		this.mapper = mapper;
	}

	@Override
	public List<Cliente> getAll() {
		return this.convertClientesPLToClientes(this.clienteRepository.findAll());
	}
	
	// ********************************************
	//
	// Private Methods
	//
	// ********************************************
	
	private List<Cliente> convertClientesPLToClientes(List<ClientePL> clientesPL) {
		return clientesPL.stream()
				.map(x -> mapper.map(x, Cliente.class))
				.toList();
	}

}
