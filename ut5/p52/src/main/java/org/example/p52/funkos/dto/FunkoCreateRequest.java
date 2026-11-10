package org.example.p52.funkos.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/** Entra en POST y PUT: todos los campos obligatorios. */
public record FunkoCreateRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede pasar de 100 caracteres")
        String nombre,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio tiene que ser mayor que 0")
        @Digits(integer = 8, fraction = 2)
        BigDecimal precio,

        @NotNull(message = "La cantidad es obligatoria")
        @PositiveOrZero(message = "La cantidad no puede ser negativa")
        Integer cantidad,

        @NotBlank(message = "La categoría es obligatoria")
        String categoria
) {}