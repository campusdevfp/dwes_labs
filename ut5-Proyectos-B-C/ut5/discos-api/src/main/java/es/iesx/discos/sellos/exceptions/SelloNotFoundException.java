package es.iesx.discos.sellos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SelloNotFoundException extends RuntimeException {
    public SelloNotFoundException(Long id) {
        super("No se ha encontrado el sello con id: " + id);
    }
}
