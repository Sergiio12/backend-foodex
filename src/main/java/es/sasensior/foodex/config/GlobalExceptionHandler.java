package es.sasensior.foodex.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import es.sasensior.foodex.presentation.utils.ApiResponseBody;
import es.sasensior.foodex.presentation.utils.PresentationException;
import es.sasensior.foodex.presentation.utils.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private static final String JSON_OBJECT_NOT_READABLE = "No se puede leer el objeto JSON.";
    private static final String HTTP_REQUEST_METHOD_NOT_SUPPORTED = "No existe end-point para atender esta petición.";
    private static final String UNEXCEPTED_SERVER_ERROR = "Se ha producido un error inesperado en el servidor.";
    private static final String ACCESS_DENIED = "No tienes permisos para realizar esta acción.";
    private static final String NO_RESOURCE_FOUND_EXCEPTION = "No se ha encontrado el recurso que buscas.";

    // **********************************************************************************
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseBody> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ApiResponseBody apiResponseBody = new ApiResponseBody.Builder(JSON_OBJECT_NOT_READABLE).status(ResponseStatus.ERROR).build();
        return ResponseEntity.badRequest().body(apiResponseBody);
    }
    
    // **********************************************************************************
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseBody> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponseBody apiResponseBody = new ApiResponseBody.Builder(ACCESS_DENIED)
             .status(ResponseStatus.ERROR)
             .build();
        return new ResponseEntity<>(apiResponseBody, HttpStatus.FORBIDDEN);
    }
    
    // **********************************************************************************
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseBody> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = "Falta un parámetro obligatorio: " + ex.getParameterName();

        ApiResponseBody response = new ApiResponseBody.Builder(message)
                .status(ResponseStatus.ERROR)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // **********************************************************************************
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponseBody> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ApiResponseBody apiResponseBody = new ApiResponseBody.Builder(HTTP_REQUEST_METHOD_NOT_SUPPORTED).status(ResponseStatus.ERROR).build();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(apiResponseBody);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String tipoRequerido = ex.getRequiredType().getSimpleName();
        String tipoEntrante = ex.getValue().getClass().getSimpleName();
        
        String respuesta = "El valor [" + ex.getValue() + "] es de tipo [" + tipoEntrante + "]. Se requiere un tipo [" + tipoRequerido + "]";
        ApiResponseBody apiResponseBody = new ApiResponseBody.Builder(respuesta).status(ResponseStatus.ERROR).build();
        
        return ResponseEntity.badRequest().body(apiResponseBody);
    }
    
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex) {
        ApiResponseBody apiResponseBody = new ApiResponseBody.Builder(NO_RESOURCE_FOUND_EXCEPTION).status(ResponseStatus.ERROR).build();
        
        return ResponseEntity.badRequest().body(apiResponseBody);
    }
    
    // **********************************************************************************
    
    @ExceptionHandler(PresentationException.class)
    public ResponseEntity<Object> handlePresentationException(PresentationException ex) {
        return new ResponseEntity<>(ex.getApiResponseBody(), ex.getHttpSatus());
    }
    
    // **********************************************************************************
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
    	logger.error(ex.getMessage());
    	
        ApiResponseBody apiResponseBody = new ApiResponseBody.Builder(UNEXCEPTED_SERVER_ERROR).status(ResponseStatus.ERROR).build();
        return ResponseEntity.internalServerError()
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(apiResponseBody);
    }
}
