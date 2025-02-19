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
				.orElseThrow(() -> new IllegalArgumentException("Ese rol no existe."));
		
		if(usuarioPLRepository.hasRole(username, Rol.valueOf(rolName))) {
			throw new IllegalStateException("El usuario ya tiene ese rol.");
		}
		
		usuarioPL.getRoles().add(rolPL);
		usuarioPLRepository.save(usuarioPL);
		
	}

	@Override
	public void removeRol(String username, String rolName) {
		
		UsuarioPL usuarioPL = usuarioPLRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("No se ha encontrado al usuario."));
		
		/*if(usuarioPLRepository.hasOnlyDefaultRole(usuarioPL.getUsername()) ) { //TODO - REVISAR, ESTA QUERY ESTÁ MAL.
			throw new IllegalStateException("No puedes eliminar ningún rol de este usuario porque solo tiene el rol por defecto.");
		}*/
		
		RolPL rolPL = rolPLRepository.findByName(rolName)
				.orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el rol."));
		
		if(!(usuarioPLRepository.hasRole(username, Rol.valueOf(rolName)))) {
			throw new IllegalStateException("El usuario no tiene el rol que estás intentando borrar.");
		}
			
		if(rolPL.getName().equals(Rol.USUARIO)) {
			throw new IllegalStateException("No se puede borrar este rol porque está establecido como el rol por defecto.");
		}
		
		usuarioPL.getRoles().remove(rolPL);
		usuarioPLRepository.save(usuarioPL);
		
	}

	@Override
	public void guardarEstado(UsuarioPL usuarioPL) {
		usuarioPLRepository.save(usuarioPL);
		
	}

}
