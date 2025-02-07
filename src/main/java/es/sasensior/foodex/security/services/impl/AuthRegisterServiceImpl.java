package es.sasensior.foodex.security.services.impl;

import org.springframework.stereotype.Service;

import es.sasensior.foodex.security.integration.model.UsuarioPL;
import es.sasensior.foodex.security.integration.repositories.UsuarioPLRepository;
import es.sasensior.foodex.security.services.AuthRegisterService;
import lombok.Getter;

@Service
@Getter //Para quitar advertencias.
public class AuthRegisterServiceImpl implements AuthRegisterService {

	private UsuarioPLRepository usuarioPLRepository; 
	
	public AuthRegisterServiceImpl(UsuarioPLRepository usuarioPLRepository) {
		this.usuarioPLRepository = usuarioPLRepository;
	}
	
	@Override
	public boolean existsUserByEmail(String email) {
		return usuarioPLRepository.existsByEmail(email);
	}
	
	@Override
	public boolean existsUserByUsername(String username) {
		return usuarioPLRepository.existsByUsername(username);
	}
	
	@Override
	public void guardarUsuarioBBDD(UsuarioPL usuario) {
		usuarioPLRepository.save(usuario);
	}

}
