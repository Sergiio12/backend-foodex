package es.sasensior.foodex.presentation.utils;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class PresentationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final HttpStatus httpSatus;
	private final ApiResponseBody apiResponseBody;
	
	private PresentationException(Builder builder) {
		super(builder.message);
		this.httpSatus = builder.httpStatus;
		this.apiResponseBody = new ApiResponseBody.Builder(builder.message).status(ResponseStatus.ERROR).data(builder.data).errors(builder.errors).build();
	}
	
	//Creamos clase interna para el patrón 'builder'.
	public static class Builder {
		private HttpStatus httpStatus;
		private String message;
		private Object data;
		private List<ErrorDetail> errors;
		
		public Builder(HttpStatus httpStatus, String message) {
			this.httpStatus = httpStatus;
			this.message = message;
		}
		
		public Builder data(Object data) {
			this.data = data;
			return this;
		}
		
		public Builder errors(List<ErrorDetail> errors) {
			this.errors = errors;
			return this;
		}
		
		public PresentationException build() {
			return new PresentationException(this);
		}
		
	}

}
