package org.example.p52.funkos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Lo que SALE. Lleva el NOMBRE de la categoria, no su id. */
public record FunkoResponse(
        Long id,
        String nombre,
        BigDecimal precio,
        Integer cantidad,
        String categoria,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}