package es.iesx.instituto.cursos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CursoConflictException extends RuntimeException {
    public CursoConflictException(String mensaje) { super(mensaje); }
}
