package es.iesx.instituto.cursos.dto;

import jakarta.validation.constraints.*;

public record CursoCreateRequest(
        @NotBlank(message = "El código es obligatorio") @Size(max = 10) String codigo,
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 120) String nombre,
        @NotNull(message = "Las plazas son obligatorias")
        @Positive(message = "Las plazas tienen que ser más de 0") Integer plazas) {}
