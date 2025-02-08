package es.sasensior.foodex.presentation.config;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


	// **********************************************************************************

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String respuesta = "No se puede leer el objeto JSON.";
		ApiResponseBody apiResponseBody = new ApiResponseBody(ResponseStatus.ERROR, respuesta, LocalDateTime.now());
		return ResponseEntity.badRequest().body(apiResponseBody);
	}

	// **********************************************************************************
	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		String respuesta = "No existe end-point para atender esta petición. ¿Estás usando el verbo correcto?";
		ApiResponseBody apiResponseBody = new ApiResponseBody(ResponseStatus.ERROR, respuesta, LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(apiResponseBody);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
	
		String tipoRequerido = ex.getRequiredType().getSimpleName();
		String tipoEntrante = ex.getValue().getClass().getSimpleName();
		
		String respuesta = "El valor [" + ex.getValue() + "] es de tipo [" + tipoEntrante + "]. Se requiere un tipo [" + tipoRequerido + "]";
		ApiResponseBody apiResponseBody = new ApiResponseBody(ResponseStatus.ERROR, respuesta, LocalDateTime.now());
		
		return ResponseEntity.badRequest().body(apiResponseBody);
	}
	
	// **********************************************************************************
	
	@ExceptionHandler(PresentationException.class) 
	public ResponseEntity<Object> handlePresentationException(PresentationException ex) {
		
		ApiResponseBody apiResponseBody = new ApiResponseBody(ResponseStatus.ERROR, ex.getMessage(), LocalDateTime.now());
		
		return new ResponseEntity<>(apiResponseBody, ex.getHttpStatus());
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex){
		
		String respuesta = "Ha ocurrido un error inesperado en el servidor.";
		ApiResponseBody apiResponseBody = new ApiResponseBody(ResponseStatus.ERROR, respuesta, LocalDateTime.now());
		
		return ResponseEntity.internalServerError().body(apiResponseBody);
	}
	

}