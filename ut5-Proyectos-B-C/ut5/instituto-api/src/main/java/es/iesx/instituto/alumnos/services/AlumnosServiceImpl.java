package es.iesx.instituto.alumnos.services;

import es.iesx.instituto.alumnos.dto.AlumnoCreateRequest;
import es.iesx.instituto.alumnos.dto.AlumnoResponse;
import es.iesx.instituto.alumnos.dto.AlumnoUpdateRequest;
import es.iesx.instituto.alumnos.exceptions.AlumnoBadRequestException;
import es.iesx.instituto.alumnos.exceptions.AlumnoConflictException;
import es.iesx.instituto.alumnos.exceptions.AlumnoNotFoundException;
import es.iesx.instituto.alumnos.mappers.AlumnoMapper;
import es.iesx.instituto.alumnos.models.Alumno;
import es.iesx.instituto.alumnos.repositories.AlumnosRepository;
import es.iesx.instituto.cursos.models.Curso;
import es.iesx.instituto.cursos.repositories.CursosRepository;
import es.iesx.instituto.notas.dto.NotaCreateRequest;
import es.iesx.instituto.notas.dto.NotaResponse;
import es.iesx.instituto.notas.mappers.NotaMapper;
import es.iesx.instituto.notas.models.Nota;
import es.iesx.instituto.notas.repositories.NotasRepository;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AlumnosServiceImpl implements AlumnosService {

    private static final Logger log = LoggerFactory.getLogger(AlumnosServiceImpl.class);

    private final AlumnosRepository repositorio;
    private final CursosRepository cursosRepository;
    private final NotasRepository notasRepository;
    private final AlumnoMapper mapper;
    private final NotaMapper notaMapper;

    public AlumnosServiceImpl(AlumnosRepository repositorio,
                              CursosRepository cursosRepository,
                              NotasRepository notasRepository,
                              AlumnoMapper mapper,
                              NotaMapper notaMapper) {
        this.repositorio = repositorio;
        this.cursosRepository = cursosRepository;
        this.notasRepository = notasRepository;
        this.mapper = mapper;
        this.notaMapper = notaMapper;
    }

    // ─────────────────────────── LECTURAS ───────────────────────────

    /** Paso 9 · los tres filtros, en la base de datos. */
    @Override
    @Transactional(readOnly = true)
    public Page<AlumnoResponse> buscar(Optional<String> curso,
                                       Optional<String> apellidos,
                                       Optional<Double> notaMin,
                                       Pageable pageable) {

        // 1 · base: los activos. SIEMPRE se aplica
        Specification<Alumno> spec = (root, query, cb) -> cb.isTrue(root.get("activo"));

        // 2 · por el CODIGO del curso: navega la relacion
        if (curso.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.upper(root.get("curso").get("codigo")),
                             curso.get().toUpperCase()));
        }

        // 3 · por apellidos: contiene
        if (apellidos.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("apellidos")),
                            "%" + apellidos.get().toLowerCase() + "%"));
        }

        // 4 · por nota media: una SUBCONSULTA dentro del WHERE
        if (notaMin.isPresent()) {
            spec = spec.and((root, query, cb) -> {
                Subquery<Double> media = query.subquery(Double.class);
                Root<Nota> nota = media.from(Nota.class);
                media.select(cb.avg(nota.<Double>get("valor")))
                     .where(cb.equal(nota.get("alumno"), root));    // las notas de ESTE alumno
                return cb.greaterThanOrEqualTo(media, notaMin.get());
            });
        }

        // 5 · la pagina, con el curso ya traido (@EntityGraph del repositorio)
        Page<Alumno> pagina = repositorio.findAll(spec, pageable);

        // 6 · las medias de los alumnos de ESTA pagina, en UNA sola consulta
        Map<Long, Double> medias = new HashMap<>();
        var ids = pagina.getContent().stream().map(Alumno::getId).toList();
        if (!ids.isEmpty()) {
            for (Object[] fila : notasRepository.mediasDe(ids)) {
                medias.put((Long) fila[0], ((Number) fila[1]).doubleValue());
            }
        }

        // 7 · y cada alumno, con su media
        return pagina.map(a -> mapper.toResponse(a, medias.get(a.getId())));
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponse findById(Long id) {
        var alumno = buscarAlumno(id);
        return mapper.toResponse(alumno, notasRepository.mediaDe(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotaResponse> notasDe(Long alumnoId) {
        buscarAlumno(alumnoId);                   // 404 si no existe
        return notaMapper.toResponse(
                notasRepository.findByAlumnoIdOrderByFechaDesc(alumnoId));
    }

    // ─────────────────────────── ESCRITURAS ─────────────────────────

    /** POST · R2, R3 y R4 */
    @Override
    @Transactional
    public AlumnoResponse save(AlumnoCreateRequest request) {

        // R2 · el expediente no se repite
        if (repositorio.existsByExpedienteIgnoreCase(request.expediente())) {
            throw new AlumnoConflictException(
                    "Ya existe el expediente " + request.expediente());
        }

        // R3 · el curso tiene que existir
        var curso = buscarCurso(request.curso());

        // R4 · y tiene que quedar sitio
        if (curso.getMatriculados() >= curso.getPlazas()) {
            throw new AlumnoConflictException(
                    "El curso %s está completo: %d de %d plazas"
                            .formatted(curso.getCodigo(), curso.getMatriculados(),
                                       curso.getPlazas()));
        }

        var guardado = repositorio.save(mapper.toModel(request, curso));

        // el contador sube: las dos cosas, o ninguna
        cursosRepository.cambiarMatriculados(curso.getId(), +1);

        log.info("Alumno matriculado: {} en {}", guardado.getExpediente(), curso.getCodigo());
        return mapper.toResponse(guardado, null);     // recien matriculado: sin notas
    }

    /** PUT · el expediente NO se cambia: el mapeador lo ignora */
    @Override
    @Transactional
    public AlumnoResponse replace(Long id, AlumnoCreateRequest request) {

        var alumno = buscarAlumno(id);
        var cursoAnterior = alumno.getCurso();
        var cursoNuevo = buscarCurso(request.curso());

        comprobarPlazaSiCambia(cursoAnterior, cursoNuevo);    // R4 · antes de tocar nada

        mapper.reemplazar(alumno, request, cursoNuevo);       // 1 · primero, los cambios
        moverContadorSiCambia(cursoAnterior, cursoNuevo);     // 2 · el @Modifying, AL FINAL

        return mapper.toResponse(alumno, notasRepository.mediaDe(id));
    }

    /** PATCH · el curso puede venir o no */
    @Override
    @Transactional
    public AlumnoResponse update(Long id, AlumnoUpdateRequest request) {

        var alumno = buscarAlumno(id);
        var cursoAnterior = alumno.getCurso();
        Curso cursoNuevo = request.curso() == null ? null : buscarCurso(request.curso());

        if (cursoNuevo != null) comprobarPlazaSiCambia(cursoAnterior, cursoNuevo);

        mapper.parchear(alumno, request, cursoNuevo);         // 1 · primero, los cambios
        if (cursoNuevo != null) moverContadorSiCambia(cursoAnterior, cursoNuevo);   // 2 · al final

        return mapper.toResponse(alumno, notasRepository.mediaDe(id));
    }

    /** DELETE · baja logica, y el contador baja */
    @Override
    @Transactional
    public void deleteById(Long id) {
        var alumno = buscarAlumno(id);

        alumno.setActivo(false);                                    // dirty checking
        cursosRepository.cambiarMatriculados(alumno.getCurso().getId(), -1);

        log.info("Alumno dado de baja: {}", id);
    }

    /** POST /alumnos/{id}/notas · R5 */
    @Override
    @Transactional
    public NotaResponse anadirNota(Long alumnoId, NotaCreateRequest request) {

        var alumno = buscarAlumno(alumnoId);

        var guardada = notasRepository.save(notaMapper.toModel(request, alumno));

        return notaMapper.toResponse(guardada);
    }

    // ─────────────────────────── AUXILIARES ─────────────────────────

    private Alumno buscarAlumno(Long id) {
        return repositorio.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new AlumnoNotFoundException(id));
    }

    private Curso buscarCurso(String codigo) {
        return cursosRepository.findByCodigoIgnoreCase(codigo)
                .orElseThrow(() -> new AlumnoBadRequestException(
                        "El curso '" + codigo + "' no existe"));
    }

    /** R4 · si cambia de curso, el nuevo tiene que tener sitio. */
    private void comprobarPlazaSiCambia(Curso anterior, Curso nuevo) {
        if (anterior.getId().equals(nuevo.getId())) return;

        if (nuevo.getMatriculados() >= nuevo.getPlazas()) {
            throw new AlumnoConflictException(
                    "El curso %s está completo".formatted(nuevo.getCodigo()));
        }
    }

    /** Si cambia de curso, el contador de los DOS cursos se mueve. */
    private void moverContadorSiCambia(Curso anterior, Curso nuevo) {
        if (anterior.getId().equals(nuevo.getId())) return;

        cursosRepository.cambiarMatriculados(anterior.getId(), -1);
        cursosRepository.cambiarMatriculados(nuevo.getId(), +1);
    }
}
