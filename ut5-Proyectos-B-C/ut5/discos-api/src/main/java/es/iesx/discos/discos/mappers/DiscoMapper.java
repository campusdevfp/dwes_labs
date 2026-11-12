package es.iesx.discos.discos.mappers;

import es.iesx.discos.artistas.models.Artista;
import es.iesx.discos.discos.dto.DiscoCreateRequest;
import es.iesx.discos.discos.dto.DiscoResponse;
import es.iesx.discos.discos.dto.DiscoUpdateRequest;
import es.iesx.discos.discos.models.Disco;
import es.iesx.discos.sellos.models.Sello;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscoMapper {

    /** Toca LAS DOS relaciones: se llama dentro de la transaccion. */
    public DiscoResponse toResponse(Disco d) {
        return new DiscoResponse(
                d.getId(), d.getTitulo(), d.getAnio(), d.getPrecio(),
                d.getArtista().getNombre(),
                d.getSello().getNombre(),
                d.getCreatedAt(), d.getUpdatedAt());
    }

    public List<DiscoResponse> toResponse(List<Disco> discos) {
        return discos.stream().map(this::toResponse).toList();
    }

    /** POST: el servicio ya ha resuelto el artista Y el sello. */
    public Disco toModel(DiscoCreateRequest r, Artista artista, Sello sello) {
        return new Disco(r.titulo().trim(), r.anio(), r.precio(), artista, sello);
    }

    /** PUT */
    public void reemplazar(Disco d, DiscoCreateRequest r, Artista artista, Sello sello) {
        d.setTitulo(r.titulo().trim());
        d.setAnio(r.anio());
        d.setPrecio(r.precio());
        d.setArtista(artista);
        d.setSello(sello);
    }

    /** PATCH: null significa "no lo cambies", tambien en las dos relaciones. */
    public void parchear(Disco d, DiscoUpdateRequest r, Artista artista, Sello sello) {
        if (r.titulo() != null) d.setTitulo(r.titulo().trim());
        if (r.anio()   != null) d.setAnio(r.anio());
        if (r.precio() != null) d.setPrecio(r.precio());
        if (artista    != null) d.setArtista(artista);
        if (sello      != null) d.setSello(sello);
    }
}
