package es.iesx.instituto.notas.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record NotaCreateRequest(
        @NotBlank(message = "La asignatura es obligatoria") @Size(max = 60) String asignatura,
        @NotNull(message = "La nota es obligatoria")
        @DecimalMin(value = "0.0", message = "La nota va de 0 a 10")
        @DecimalMax(value = "10.0", message = "La nota va de 0 a 10") Double valor,
        @NotNull(message = "La fecha es obligatoria")
        @PastOrPresent(message = "La fecha no puede ser futura") LocalDate fecha) {}
