package es.iesx.instituto.cursos.repositories;

import es.iesx.instituto.cursos.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CursosRepository extends JpaRepository<Curso, Long> {

    Optional<Curso> findByCodigoIgnoreCase(String codigo);

    boolean existsByCodigoIgnoreCase(String codigo);

    /** Suma o resta uno al contador, sin leer la entidad. */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Curso c SET c.matriculados = c.matriculados + :incremento WHERE c.id = :id")
    int cambiarMatriculados(@Param("id") Long id, @Param("incremento") int incremento);
}
