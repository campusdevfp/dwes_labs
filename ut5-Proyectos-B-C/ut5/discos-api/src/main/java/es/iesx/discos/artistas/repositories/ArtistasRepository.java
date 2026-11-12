package es.iesx.discos.artistas.repositories;

import es.iesx.discos.artistas.models.Artista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistasRepository extends JpaRepository<Artista, Long> {

    Optional<Artista> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    Page<Artista> findByPaisIgnoreCase(String pais, Pageable pageable);
}
