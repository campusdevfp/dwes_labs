package es.iesx.instituto.alumnos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlumnoConflictException extends RuntimeException {
    public AlumnoConflictException(String mensaje) { super(mensaje); }
}
