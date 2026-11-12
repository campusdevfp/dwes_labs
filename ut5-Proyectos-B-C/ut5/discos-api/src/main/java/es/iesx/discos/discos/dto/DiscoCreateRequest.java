package es.iesx.discos.discos.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record DiscoCreateRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 200) String titulo,

        @NotNull(message = "El año es obligatorio")
        @Min(value = 1900, message = "El año no puede ser anterior a 1900")
        @Max(value = 2100, message = "El año no puede ser posterior a 2100") Integer anio,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio tiene que ser mayor que 0")
        BigDecimal precio,

        @NotBlank(message = "El artista es obligatorio") String artista,
        @NotBlank(message = "El sello es obligatorio")   String sello) {}
