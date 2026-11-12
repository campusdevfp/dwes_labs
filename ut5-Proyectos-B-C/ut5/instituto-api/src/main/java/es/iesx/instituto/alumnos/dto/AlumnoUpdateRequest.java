package es.iesx.instituto.alumnos.dto;

import jakarta.validation.constraints.Size;

/** PATCH: todo opcional. El expediente no se cambia nunca. */
public record AlumnoUpdateRequest(
        @Size(max = 80)  String nombre,
        @Size(max = 120) String apellidos,
        String curso) {}
