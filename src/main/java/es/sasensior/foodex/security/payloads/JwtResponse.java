package es.sasensior.foodex.security.payloads;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class JwtResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String token;
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
	
	public JwtResponse(String token, Long id, String username, String email, List<String> roles) {
		this.token = token;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

}
