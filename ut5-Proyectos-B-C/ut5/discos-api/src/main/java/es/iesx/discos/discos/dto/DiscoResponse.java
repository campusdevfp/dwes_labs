package es.iesx.discos.discos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DiscoResponse(
        Long id, String titulo, Integer anio, BigDecimal precio,
        String artista, String sello,
        LocalDateTime createdAt, LocalDateTime updatedAt) {}
