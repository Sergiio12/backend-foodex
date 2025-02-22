package es.sasensior.foodex.security;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${foodex.app.jwt-secret}")
    private String jwtSecret; //Clave secreta para firmar los tokens.

    @Value("${foodex.app.jwt-expiration-ms}")
    private int jwtExpirationMs; //Tiempo de expiración del token.

    public String generateJwtToken(Authentication authentication) {

        UsuarioPL usuarioPL = (UsuarioPL) authentication.getPrincipal();

        List<String> roles = usuarioPL.getAuthorities().stream().map(x -> x.toString()).toList(); //Extrae todos los roles del usuario:
        
        String nombreCompleto = usuarioPL.getFirstName() + " " + usuarioPL.getLastName();

        return Jwts.builder()
                .setSubject(usuarioPL.getUsername()) //Define el nombre de usuario como el dueño del token.
                .claim("roles", roles) //Añade los roles al token.
                .claim("nombre", nombreCompleto) //Añade el nombre completo al token.
                .setIssuedAt(new Date(System.currentTimeMillis())) //Fecha de emisión.
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) //Fecha de expiracion
                .signWith(key(), SignatureAlgorithm.HS256) //Firma con clave secreta.
                .compact(); //Construye el token final con formado String
    }
    
    /**
     * Toma un token JWT y extrae el nombre de usuario (subject en el payload del token).
     * @param token es el token que toma
     * @return el nombre del usuario al que pertenece ese token
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Este método verifica si un token JWT es válido.
     * @param token es el token que toma por parámetros.
     * @return si el token es válido o no.
     */
    public boolean validateJwtToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage()); //Token mal formado (cortado o modificado).
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage()); //Token expirado.
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage()); //Algoritmo de firma no soportado.
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage()); //Token vacío
        } catch(SignatureException e) {
        	logger.error("JWT signature does not match: {}", e.getMessage());
        }

        return false;
    }

    // *************************************************************************************
    //
    // PRIVATE METHODS
    //
    // *************************************************************************************
   
    /**
     * Convierte la clave secreta de String a un Key utilizando BASE64.decode(). Es necesario porque JWT usa claves Key en formato binario, no cadenas de texto.
     * @return la clave convertida.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


}
