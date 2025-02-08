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
	private final List<ErrorDetail> lErrors;

	public ApiResponseBody(ResponseStatus status, String message, LocalDateTime timestamp) {
		this.status = status.toString().toLowerCase();
		this.message = message;
		this.timestamp = timestamp;
		this.data = null;
		this.lErrors = null;
	}
	
	public ApiResponseBody(ResponseStatus status, String message, LocalDateTime timestamp, Object data, List<ErrorDetail> lErrors) {
		this.status = status.toString().toLowerCase();
		this.message = message;
		this.timestamp = null;
		this.data = data;
		this.lErrors = lErrors;
	}
	
}
