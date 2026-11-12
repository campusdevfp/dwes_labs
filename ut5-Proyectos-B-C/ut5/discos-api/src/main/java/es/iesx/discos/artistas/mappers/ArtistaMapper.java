package es.iesx.discos.artistas.mappers;

import es.iesx.discos.artistas.dto.ArtistaCreateRequest;
import es.iesx.discos.artistas.dto.ArtistaResponse;
import es.iesx.discos.artistas.models.Artista;
import org.springframework.stereotype.Component;

@Component
public class ArtistaMapper {
    public ArtistaResponse toResponse(Artista a, long numeroDeDiscos) {
        return new ArtistaResponse(a.getId(), a.getNombre(), a.getPais(), numeroDeDiscos);
    }
    public Artista toModel(ArtistaCreateRequest r) {
        return new Artista(r.nombre().trim(), r.pais().trim());
    }
}
