package es.iesx.discos.discos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DiscoConflictException extends RuntimeException {
    public DiscoConflictException(String mensaje) { super(mensaje); }
}
