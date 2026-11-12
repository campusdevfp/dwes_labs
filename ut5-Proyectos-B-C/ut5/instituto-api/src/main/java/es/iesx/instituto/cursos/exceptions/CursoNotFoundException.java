package es.iesx.instituto.cursos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CursoNotFoundException extends RuntimeException {
    public CursoNotFoundException(Long id) {
        super("No se ha encontrado el curso con id: " + id);
    }
}
