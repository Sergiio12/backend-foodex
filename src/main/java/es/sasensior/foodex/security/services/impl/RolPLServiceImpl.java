package es.sasensior.foodex.security.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import es.sasensior.foodex.security.integration.dao.RolPL;
import es.sasensior.foodex.security.integration.repositories.RolPLRepository;
import es.sasensior.foodex.security.services.RolPLService;

@Service
public class RolPLServiceImpl implements RolPLService {

	private final RolPLRepository rolPLRepository;
	
	public RolPLServiceImpl(RolPLRepository rolPLRepository) {
		this.rolPLRepository = rolPLRepository;
	}
	
	@Override
	public Optional<RolPL> getRol(String nombreRol) {
		return rolPLRepository.findByName(nombreRol);
	}

}
