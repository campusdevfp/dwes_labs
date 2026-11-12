package es.iesx.instituto.alumnos.dto;

import jakarta.validation.constraints.*;

public record AlumnoCreateRequest(
        @NotBlank(message = "El expediente es obligatorio")
        @Size(min = 4, max = 12, message = "El expediente tiene de 4 a 12 caracteres") String expediente,
        @NotBlank(message = "El nombre es obligatorio")    @Size(max = 80)  String nombre,
        @NotBlank(message = "Los apellidos son obligatorios") @Size(max = 120) String apellidos,
        @NotBlank(message = "El curso es obligatorio") String curso) {}
