package es.iesx.discos.artistas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ArtistaCreateRequest(
        @NotBlank @Size(max = 120) String nombre,
        @NotBlank @Size(max = 60)  String pais) {}
