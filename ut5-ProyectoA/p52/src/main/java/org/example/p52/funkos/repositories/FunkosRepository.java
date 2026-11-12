package org.example.p52.funkos.repositories;


import org.example.p52.funkos.models.Funko;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FunkosRepository
        extends JpaRepository<Funko, Long>,
        JpaSpecificationExecutor<Funko> {          // ← para los filtros del paso 11

    // ── derivadas del nombre: sin cuerpo ──────────────────────────────
    Optional<Funko> findByIdAndDeletedFalse(Long id);

    boolean existsByNombreIgnoreCaseAndDeletedFalse(String nombre);

    long countByCategoriaIdAndDeletedFalse(Long categoriaId);

    /** Listado paginado, trayendo la categoria en la MISMA consulta. */
    @EntityGraph(attributePaths = {"categoria"})
    Page<Funko> findByDeletedFalse(Pageable pageable);

    /**
     * El buscador del paso 11. Este metodo YA lo trae JpaSpecificationExecutor:
     * lo volvemos a declarar SOLO para ponerle el @EntityGraph.
     */
    @Override
    @EntityGraph(attributePaths = {"categoria"})
    Page<Funko> findAll(Specification<Funko> spec, Pageable pageable);

    // ── borrado logico ────────────────────────────────────────────────
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           UPDATE Funko f
              SET f.deleted = true, f.updatedAt = CURRENT_TIMESTAMP
            WHERE f.id = :id AND f.deleted = false
           """)
    int borradoLogico(@Param("id") Long id);
}