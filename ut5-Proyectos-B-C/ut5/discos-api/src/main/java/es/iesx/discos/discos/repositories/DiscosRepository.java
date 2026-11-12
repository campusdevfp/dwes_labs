package es.iesx.discos.discos.repositories;

import es.iesx.discos.discos.models.Disco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DiscosRepository
        extends JpaRepository<Disco, Long>,
                JpaSpecificationExecutor<Disco> {

    Optional<Disco> findByIdAndDeletedFalse(Long id);

    boolean existsByTituloIgnoreCaseAndArtistaIdAndDeletedFalse(String titulo, Long artistaId);

    long countByArtistaIdAndDeletedFalse(Long artistaId);

    long countBySelloIdAndDeletedFalse(Long selloId);

    /** Trae LAS DOS relaciones en la misma consulta. */
    @EntityGraph(attributePaths = {"artista", "sello"})
    Page<Disco> findByDeletedFalse(Pageable pageable);

    /** El buscador del paso 9: se redeclara SOLO para ponerle el @EntityGraph. */
    @Override
    @EntityGraph(attributePaths = {"artista", "sello"})
    Page<Disco> findAll(Specification<Disco> spec, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           UPDATE Disco d
              SET d.deleted = true, d.updatedAt = CURRENT_TIMESTAMP
            WHERE d.id = :id AND d.deleted = false
           """)
    int borradoLogico(@Param("id") Long id);
}
