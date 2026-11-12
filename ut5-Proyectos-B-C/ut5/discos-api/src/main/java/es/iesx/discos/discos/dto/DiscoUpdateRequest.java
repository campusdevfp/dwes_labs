package es.iesx.discos.discos.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/** PATCH: todo opcional, mismas reglas de formato. */
public record DiscoUpdateRequest(
        @Size(max = 200) String titulo,
        @Min(1900) @Max(2100) Integer anio,
        @DecimalMin(value = "0.0", inclusive = false) BigDecimal precio,
        String artista,
        String sello) {}
