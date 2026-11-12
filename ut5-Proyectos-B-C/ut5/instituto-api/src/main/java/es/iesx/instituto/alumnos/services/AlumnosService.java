package es.iesx.instituto.alumnos.services;

import es.iesx.instituto.alumnos.dto.AlumnoCreateRequest;
import es.iesx.instituto.alumnos.dto.AlumnoResponse;
import es.iesx.instituto.alumnos.dto.AlumnoUpdateRequest;
import es.iesx.instituto.notas.dto.NotaCreateRequest;
import es.iesx.instituto.notas.dto.NotaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AlumnosService {
    Page<AlumnoResponse> buscar(Optional<String> curso,
                                Optional<String> apellidos,
                                Optional<Double> notaMin,
                                Pageable pageable);
    AlumnoResponse findById(Long id);
    AlumnoResponse save(AlumnoCreateRequest request);
    AlumnoResponse replace(Long id, AlumnoCreateRequest request);
    AlumnoResponse update(Long id, AlumnoUpdateRequest request);
    void deleteById(Long id);

    List<NotaResponse> notasDe(Long alumnoId);
    NotaResponse anadirNota(Long alumnoId, NotaCreateRequest request);
}
