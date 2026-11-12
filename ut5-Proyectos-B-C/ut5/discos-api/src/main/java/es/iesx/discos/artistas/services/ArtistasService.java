package es.iesx.discos.artistas.services;

import es.iesx.discos.artistas.dto.ArtistaCreateRequest;
import es.iesx.discos.artistas.dto.ArtistaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ArtistasService {
    Page<ArtistaResponse> findAll(Optional<String> pais, Pageable pageable);
    ArtistaResponse findById(Long id);
    ArtistaResponse save(ArtistaCreateRequest request);
    void deleteById(Long id);
}
