package es.iesx.instituto.notas.repositories;

import es.iesx.instituto.notas.models.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotasRepository extends JpaRepository<Nota, Long> {

    List<Nota> findByAlumnoIdOrderByFechaDesc(Long alumnoId);

    long countByAlumnoId(Long alumnoId);

    /** Nota media de UN alumno. Devuelve null si no tiene notas. */
    @Query("SELECT avg(n.valor) FROM Nota n WHERE n.alumno.id = :alumnoId")
    Double mediaDe(@Param("alumnoId") Long alumnoId);

    /**
     * Las medias de VARIOS alumnos de una vez: para el listado paginado.
     * Cada fila es [ id del alumno, su media ].
     */
    @Query("""
           SELECT n.alumno.id, avg(n.valor)
             FROM Nota n
            WHERE n.alumno.id IN :ids
            GROUP BY n.alumno.id
           """)
    List<Object[]> mediasDe(@Param("ids") List<Long> ids);

    /**
     * ESTADISTICAS POR CURSO: lo calcula la base de datos, no Java.
     * Devuelve, por cada curso: codigo, nº de notas y nota media.
     */
    @Query("""
           SELECT n.alumno.curso.codigo, count(n), avg(n.valor)
             FROM Nota n
            WHERE n.alumno.activo = true
            GROUP BY n.alumno.curso.codigo
            ORDER BY n.alumno.curso.codigo
           """)
    List<Object[]> estadisticasPorCurso();
}
