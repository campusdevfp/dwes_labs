package es.iesx.instituto.cursos.mappers;

import es.iesx.instituto.cursos.dto.CursoCreateRequest;
import es.iesx.instituto.cursos.dto.CursoResponse;
import es.iesx.instituto.cursos.dto.EstadisticaResponse;
import es.iesx.instituto.cursos.models.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    public CursoResponse toResponse(Curso c) {
        return new CursoResponse(c.getId(), c.getCodigo(), c.getNombre(),
                c.getPlazas(), c.getMatriculados(),
                c.getPlazas() - c.getMatriculados());      // plazas libres, calculado
    }

    public Curso toModel(CursoCreateRequest r) {
        return new Curso(r.codigo().trim().toUpperCase(), r.nombre().trim(), r.plazas());
    }

    /** La fila del @Query de agregado -> DTO. */
    public EstadisticaResponse toEstadistica(Object[] fila) {
        return new EstadisticaResponse(
                (String) fila[0],
                ((Number) fila[1]).longValue(),
                fila[2] == null ? null : ((Number) fila[2]).doubleValue());
    }
}
