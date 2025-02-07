package es.sasensior.foodex.security;

// Importaciones necesarias para la gestión de usuarios en Spring Security
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
@Service // Marca esta clase como un servicio gestionado por Spring.
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inyección del repositorio que permite acceder a los usuarios en la base de datos.
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
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username " + username));

        // Convierte el usuario obtenido en una instancia de UserDetailsImpl para que Spring Security pueda manejarlo.
        return UserDetailsImpl.build(usuarioPL);
    }
}
