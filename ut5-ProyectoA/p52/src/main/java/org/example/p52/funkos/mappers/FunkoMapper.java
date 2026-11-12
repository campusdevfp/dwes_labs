package org.example.p52.funkos.mappers;


import org.example.p52.categorias.models.Categoria;
import org.example.p52.funkos.dto.FunkoCreateRequest;
import org.example.p52.funkos.dto.FunkoResponse;
import org.example.p52.funkos.dto.FunkoUpdateRequest;
import org.example.p52.funkos.models.Funko;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FunkoMapper {

    /** Entidad -> DTO. Toca getCategoria(): se llama DENTRO de la transaccion. */
    public FunkoResponse toResponse(Funko f) {
        return new FunkoResponse(
                f.getId(), f.getNombre(), f.getPrecio(), f.getCantidad(),
                f.getCategoria().getNombre(),
                f.getCreatedAt(), f.getUpdatedAt());
    }

    public List<FunkoResponse> toResponse(List<Funko> funkos) {
        return funkos.stream().map(this::toResponse).toList();
    }

    /** POST: entidad nueva. El servicio ya ha resuelto la categoria. */
    public Funko toModel(FunkoCreateRequest r, Categoria categoria) {
        return new Funko(r.nombre().trim(), r.precio(), r.cantidad(), categoria);
    }

    /** PUT: reemplaza TODOS los campos editables. */
    public void reemplazar(Funko funko, FunkoCreateRequest r, Categoria categoria) {
        funko.setNombre(r.nombre().trim());
        funko.setPrecio(r.precio());
        funko.setCantidad(r.cantidad());
        funko.setCategoria(categoria);
    }

    /** PATCH: solo lo que llega distinto de null. */
    public void parchear(Funko funko, FunkoUpdateRequest r, Categoria categoria) {
        if (r.nombre()   != null) funko.setNombre(r.nombre().trim());
        if (r.precio()   != null) funko.setPrecio(r.precio());
        if (r.cantidad() != null) funko.setCantidad(r.cantidad());
        if (categoria    != null) funko.setCategoria(categoria);
    }
}