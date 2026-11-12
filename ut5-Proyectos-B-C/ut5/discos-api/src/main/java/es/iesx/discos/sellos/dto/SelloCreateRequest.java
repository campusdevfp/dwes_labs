package es.iesx.discos.sellos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SelloCreateRequest(@NotBlank @Size(max = 80) String nombre) {}
