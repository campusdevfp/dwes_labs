package es.iesx.instituto.notas.dto;

import java.time.LocalDate;

public record NotaResponse(Long id, String asignatura, Double valor, LocalDate fecha) {}
