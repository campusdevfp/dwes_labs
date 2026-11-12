package es.iesx.discos.artistas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ArtistaConflictException extends RuntimeException {
    public ArtistaConflictException(String mensaje) { super(mensaje); }
}
