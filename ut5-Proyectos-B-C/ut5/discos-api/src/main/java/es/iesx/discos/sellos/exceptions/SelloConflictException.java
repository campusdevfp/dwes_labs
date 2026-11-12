package es.iesx.discos.sellos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SelloConflictException extends RuntimeException {
    public SelloConflictException(String mensaje) { super(mensaje); }
}
