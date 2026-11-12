package es.iesx.instituto.cursos.services;

import es.iesx.instituto.cursos.dto.*;
import org.springframework.data.domain.*;

import java.util.List;

public interface CursosService {
    Page<CursoResponse> findAll(Pageable pageable);
    CursoResponse findById(Long id);
    CursoResponse save(CursoCreateRequest request);
    void deleteById(Long id);

    /** Nota media y recuento de notas POR CURSO. */
    List<EstadisticaResponse> estadisticas();
}
