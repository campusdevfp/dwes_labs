package es.iesx.discos.discos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DiscoBadRequestException extends RuntimeException {
    public DiscoBadRequestException(String mensaje) { super(mensaje); }
}
