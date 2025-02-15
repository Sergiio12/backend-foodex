package es.sasensior.foodex.security.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import es.sasensior.foodex.presentation.config.PresentationException;
import es.sasensior.foodex.security.JwtUtils;
import es.sasensior.foodex.security.integration.model.RolPL;
import es.sasensior.foodex.security.integration.model.UsuarioPL;
import es.sasensior.foodex.security.integration.repositories.RolPLRepository;
import es.sasensior.foodex.security.integration.repositories.UsuarioPLRepository;
import es.sasensior.foodex.security.services.UsuarioPLService;
import lombok.Getter;

@Service
@Getter //Para quitar advertencias.
public class UsuarioPLServiceImpl implements UsuarioPLService {

	private UsuarioPLRepository usuarioPLRepository;
	private RolPLRepository rolPLRepository;
	private JwtUtils jwtUtils; 
	
	public UsuarioPLServiceImpl(UsuarioPLRepository usuarioPLRepository, RolPLRepository rolPLRepository, JwtUtils jwtUtils) {
		this.usuarioPLRepository = usuarioPLRepository;
		this.rolPLRepository = rolPLRepository;
		this.jwtUtils = jwtUtils;
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
	public void setRol(String username, String rolName) {
		
		UsuarioPL usuarioPL = usuarioPLRepository.findByUsername(username)
				.orElseThrow(() -> new PresentationException.Builder(HttpStatus.BAD_REQUEST, "No se ha encontrado al usuario.").build());
		
		RolPL rolPL = rolPLRepository.findByName(rolName)
				.orElseThrow(() -> new PresentationException.Builder(HttpStatus.BAD_REQUEST, "No se ha encontrado el rol.").build());
		
		usuarioPL.getRoles().add(rolPL);
		usuarioPLRepository.save(usuarioPL); //Con esto lo actualizamos.
		
	}

	@Override
	public void removeRol(String username, String rolName) {
		
		UsuarioPL usuarioPL = usuarioPLRepository.findByUsername(username)
				.orElseThrow(() -> new PresentationException.Builder(HttpStatus.BAD_REQUEST, "No se ha encontrado al usuario.").build());
		
		RolPL rolPL = rolPLRepository.findByName(rolName)
				.orElseThrow(() -> new PresentationException.Builder(HttpStatus.BAD_REQUEST, "No se ha encontrado el rol.").build());
		
	}

}
