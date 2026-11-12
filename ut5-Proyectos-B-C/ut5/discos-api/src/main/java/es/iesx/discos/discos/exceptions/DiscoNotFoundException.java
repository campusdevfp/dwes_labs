package es.iesx.discos.discos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DiscoNotFoundException extends RuntimeException {
    public DiscoNotFoundException(Long id) {
        super("No se ha encontrado el disco con id: " + id);
    }
}
