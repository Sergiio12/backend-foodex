package es.sasensior.foodex.presentation.config;

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
	
	private static final String JSON_OBJECT_NOT_READABLE = "No se puede leer el objeto JSON.";
	private static final String HTTP_REQUEST_METHOD_NOT_SUPPORTED = "No existe end-point para atender esta petición. ¿Estás usando el verbo correcto?";
	private static final String UNEXCEPTED_SERVER_ERROR = "Se ha producido un error inesperado en el servidor.";
	


	// **********************************************************************************

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ApiResponseBody apiResponseBody = new ApiResponseBody(ResponseStatus.ERROR, JSON_OBJECT_NOT_READABLE);
		return ResponseEntity.badRequest().body(apiResponseBody);
	}

	// **********************************************************************************
	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ApiResponseBody apiResponseBody = new ApiResponseBody(ResponseStatus.ERROR, HTTP_REQUEST_METHOD_NOT_SUPPORTED);
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(apiResponseBody);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
		String tipoRequerido = ex.getRequiredType().getSimpleName();
		String tipoEntrante = ex.getValue().getClass().getSimpleName();
		
		String respuesta = "El valor [" + ex.getValue() + "] es de tipo [" + tipoEntrante + "]. Se requiere un tipo [" + tipoRequerido + "]";
		ApiResponseBody apiResponseBody = new ApiResponseBody(ResponseStatus.ERROR, respuesta);
		
		return ResponseEntity.badRequest().body(apiResponseBody);
	}
	
	// **********************************************************************************
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex){
		ApiResponseBody apiResponseBody = new ApiResponseBody(ResponseStatus.ERROR, UNEXCEPTED_SERVER_ERROR);
		
		return ResponseEntity.internalServerError().body(apiResponseBody);
	}
	

}