package org.example.p52.categorias.mappers;

import org.example.p52.categorias.dto.CategoriaCreateRequest;
import org.example.p52.categorias.dto.CategoriaResponse;
import org.example.p52.categorias.models.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public CategoriaResponse toResponse(Categoria c, long numeroDeFunkos) {
        return new CategoriaResponse(c.getId(), c.getNombre(), numeroDeFunkos);
    }

    public Categoria toModel(CategoriaCreateRequest r) {
        return new Categoria(r.nombre().trim().toUpperCase());
    }
}