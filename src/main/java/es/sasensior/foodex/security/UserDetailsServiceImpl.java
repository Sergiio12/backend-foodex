package es.sasensior.foodex.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.sasensior.foodex.security.integration.model.UsuarioPL;
import es.sasensior.foodex.security.integration.repositories.UsuarioPLRepository;

/**
 * Implementación del servicio de usuario de Spring Security.
 * Esta clase se encarga de obtener los detalles del usuario desde la base de datos.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioPLRepository usuarioPLRepository;

    /**
     * Carga un usuario desde la base de datos por su nombre de usuario.
     * 
     * @param username El nombre de usuario a buscar.
     * @return Un objeto UserDetails que contiene la información del usuario para Spring Security.
     * @throws UsernameNotFoundException si el usuario no es encontrado en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Busca el usuario en la base de datos a través del repositorio.
        UsuarioPL usuarioPL = usuarioPLRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));

        // Convierte el usuario obtenido en una instancia de UserDetailsImpl para que Spring Security pueda manejarlo.
        return UserDetailsImpl.build(usuarioPL);
    }
}
