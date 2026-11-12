package es.iesx.instituto.cursos.services;

import es.iesx.instituto.alumnos.repositories.AlumnosRepository;
import es.iesx.instituto.cursos.dto.*;
import es.iesx.instituto.cursos.exceptions.*;
import es.iesx.instituto.cursos.mappers.CursoMapper;
import es.iesx.instituto.cursos.repositories.CursosRepository;
import es.iesx.instituto.notas.repositories.NotasRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CursosServiceImpl implements CursosService {

    private final CursosRepository repositorio;
    private final AlumnosRepository alumnosRepository;
    private final NotasRepository notasRepository;
    private final CursoMapper mapper;

    public CursosServiceImpl(CursosRepository repositorio,
                             AlumnosRepository alumnosRepository,
                             NotasRepository notasRepository,
                             CursoMapper mapper) {
        this.repositorio = repositorio;
        this.alumnosRepository = alumnosRepository;
        this.notasRepository = notasRepository;
        this.mapper = mapper;
    }

    // ─────────────────────────── LECTURAS ───────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<CursoResponse> findAll(Pageable pageable) {
        return repositorio.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponse findById(Long id) {
        return mapper.toResponse(repositorio.findById(id)
                .orElseThrow(() -> new CursoNotFoundException(id)));
    }

    /** Lo calcula la base de datos: aqui solo se convierte cada fila en un DTO. */
    @Override
    @Transactional(readOnly = true)
    public List<EstadisticaResponse> estadisticas() {
        return notasRepository.estadisticasPorCurso().stream()
                .map(mapper::toEstadistica)
                .toList();
    }

    // ─────────────────────────── ESCRITURAS ─────────────────────────

    /** R2 · el codigo del curso no se repite */
    @Override
    @Transactional
    public CursoResponse save(CursoCreateRequest request) {
        if (repositorio.existsByCodigoIgnoreCase(request.codigo())) {
            throw new CursoConflictException("Ya existe el curso " + request.codigo());
        }
        return mapper.toResponse(repositorio.save(mapper.toModel(request)));
    }

    /** R6 · un curso con alumnos activos NO se borra */
    @Override
    @Transactional
    public void deleteById(Long id) {
        var curso = repositorio.findById(id)
                .orElseThrow(() -> new CursoNotFoundException(id));

        long alumnos = alumnosRepository.countByCursoIdAndActivoTrue(id);
        if (alumnos > 0) {
            throw new CursoConflictException(
                    "No se puede borrar %s: tiene %d alumnos matriculados"
                            .formatted(curso.getCodigo(), alumnos));
        }

        repositorio.deleteById(id);
    }
}
