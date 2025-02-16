package es.sasensior.foodex.security.services.impl;

import org.springframework.stereotype.Service;

import es.sasensior.foodex.security.integration.model.Rol;
import es.sasensior.foodex.security.integration.model.RolPL;
import es.sasensior.foodex.security.integration.model.UsuarioPL;
import es.sasensior.foodex.security.integration.repositories.RolPLRepository;
import es.sasensior.foodex.security.integration.repositories.UsuarioPLRepository;
import es.sasensior.foodex.security.services.UsuarioPLService;

@Service
public class UsuarioPLServiceImpl implements UsuarioPLService {

	private final UsuarioPLRepository usuarioPLRepository;
	private final RolPLRepository rolPLRepository;
	
	public UsuarioPLServiceImpl(UsuarioPLRepository usuarioPLRepository, RolPLRepository rolPLRepository) {
		this.usuarioPLRepository = usuarioPLRepository;
		this.rolPLRepository = rolPLRepository;
	}
	
	@Override
	public boolean existsUserByUsername(String username) {
		return usuarioPLRepository.existsByUsername(username);
	}

	@Override
	public boolean existsUserByEmail(String email) {
		return usuarioPLRepository.existsByEmail(email);
	}
	
	@Override
	public void register(UsuarioPL usuario) {
		usuarioPLRepository.save(usuario);
	}

	@Override
	public void addRol(String username, String rolName) {
		
		UsuarioPL usuarioPL = usuarioPLRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("No se ha encontrado al usuario."));
		
		RolPL rolPL = rolPLRepository.findByName(rolName)
				.orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el rol."));
		
		usuarioPL.getRoles().add(rolPL);
		usuarioPLRepository.save(usuarioPL); //Con esto lo actualizamos.
		
	}

	@Override
	public void removeRol(String username, String rolName) {
		
		UsuarioPL usuarioPL = usuarioPLRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("No se ha encontrado al usuario."));
		
		RolPL rolPL = rolPLRepository.findByName(rolName)
				.orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el rol."));
			
		if(rolPL.getName().equals(Rol.USUARIO)) {
			throw new IllegalStateException("No se puede borrar este rol porque está establecido como el rol por defecto.");
		}
		
		usuarioPL.getRoles().remove(rolPL);
		usuarioPLRepository.save(usuarioPL);
		
	}

}
