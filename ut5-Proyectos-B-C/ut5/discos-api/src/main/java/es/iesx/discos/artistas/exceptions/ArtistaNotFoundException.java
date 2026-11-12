package es.iesx.discos.artistas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArtistaNotFoundException extends RuntimeException {
    public ArtistaNotFoundException(Long id) {
        super("No se ha encontrado el artista con id: " + id);
    }
}
