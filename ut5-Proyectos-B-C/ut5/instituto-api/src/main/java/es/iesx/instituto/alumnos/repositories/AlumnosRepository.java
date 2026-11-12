package es.iesx.instituto.alumnos.repositories;

import es.iesx.instituto.alumnos.models.Alumno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AlumnosRepository
        extends JpaRepository<Alumno, Long>,
                JpaSpecificationExecutor<Alumno> {

    Optional<Alumno> findByIdAndActivoTrue(Long id);

    boolean existsByExpedienteIgnoreCase(String expediente);

    long countByCursoIdAndActivoTrue(Long cursoId);

    /** Listado paginado trayendo el curso en la MISMA consulta. */
    @EntityGraph(attributePaths = {"curso"})
    Page<Alumno> findByActivoTrue(Pageable pageable);

    /** El buscador del paso 9: se redeclara SOLO para ponerle el @EntityGraph. */
    @Override
    @EntityGraph(attributePaths = {"curso"})
    Page<Alumno> findAll(Specification<Alumno> spec, Pageable pageable);
}
