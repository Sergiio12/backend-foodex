package es.sasensior.foodex.presentation.utils;

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

    private ApiResponseBody(Builder builder) {
        this.status = builder.status.toString().toLowerCase();
        this.message = builder.message;
        this.timestamp = LocalDateTime.now();
        this.data = builder.data;
        this.errors = builder.errors;
    }

    public static class Builder { //Patrón builder.
        private String message;
        private ResponseStatus status;
        private Object data;
        private List<ErrorDetail> errors;
        
        public Builder(String message) {
            this.message = message;
        }
        
        public Builder status(ResponseStatus status) {
            this.status = status;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public Builder errors(List<ErrorDetail> errors) {
            this.errors = errors;
            return this;
        }

        public ApiResponseBody build() {
            return new ApiResponseBody(this);
        }
    }
}
