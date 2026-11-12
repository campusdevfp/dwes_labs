package es.iesx.instituto.alumnos.dto;

public record AlumnoResponse(
        Long id, String expediente, String nombre, String apellidos,
        String cursoCodigo, String cursoNombre,
        Double notaMedia, boolean activo) {}
