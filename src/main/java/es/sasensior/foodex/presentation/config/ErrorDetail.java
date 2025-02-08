package es.sasensior.foodex.presentation.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetail {
	
	private String field;
	private String message;
	
	public ErrorDetail(String field, String message) {
		this.field = field;
		this.message = message; 
	}
	
	public ErrorDetail(String message) {
		this.message = message;
		this.field = null;
	}

}
