package es.iesx.instituto.cursos.dto;

public record CursoResponse(
        Long id, String codigo, String nombre,
        Integer plazas, Integer matriculados, Integer libres) {}
