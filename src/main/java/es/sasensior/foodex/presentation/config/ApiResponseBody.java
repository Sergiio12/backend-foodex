package es.sasensior.foodex.presentation.config;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseBody {
	
	private final String status;
	private final String message;
	private final LocalDateTime timestamp;
	private final Object data;
	private final List<ErrorDetail> errors;

	public ApiResponseBody(ResponseStatus status, String message) {
		this.status = status.toString().toLowerCase();
		this.message = message;
		this.timestamp = LocalDateTime.now();
		this.data = null;
		this.errors = null;
	}
	
	public ApiResponseBody(ResponseStatus status, String message, Object data, List<ErrorDetail> lErrors) {
		this.status = status.toString().toLowerCase();
		this.message = message;
		this.timestamp = LocalDateTime.now();
		this.data = data;
		this.errors = lErrors;
	}
	
	public ApiResponseBody(ResponseStatus status, String message, Object data) {
		this.status = status.toString().toLowerCase();
		this.message = message;
		this.timestamp = LocalDateTime.now();
		this.data = data;
		this.errors = null;
	}

}
