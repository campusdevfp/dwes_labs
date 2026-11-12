package org.example.p52.funkos.services;


import org.example.p52.categorias.models.Categoria;
import org.example.p52.categorias.repositories.CategoriasRepository;
import org.example.p52.funkos.dto.FunkoCreateRequest;
import org.example.p52.funkos.dto.FunkoResponse;
import org.example.p52.funkos.dto.FunkoUpdateRequest;
import org.example.p52.funkos.exceptions.FunkoBadRequestException;
import org.example.p52.funkos.exceptions.FunkoConflictException;
import org.example.p52.funkos.exceptions.FunkoNotFoundException;
import org.example.p52.funkos.mappers.FunkoMapper;
import org.example.p52.funkos.models.Funko;
import org.example.p52.funkos.repositories.FunkosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class FunkosServiceImpl implements FunkosService {

    private static final Logger log = LoggerFactory.getLogger(FunkosServiceImpl.class);

    private final FunkosRepository repositorio;
    private final CategoriasRepository categoriasRepository;
    private final FunkoMapper mapper;

    public FunkosServiceImpl(FunkosRepository repositorio,
                             CategoriasRepository categoriasRepository,
                             FunkoMapper mapper) {
        this.repositorio = repositorio;
        this.categoriasRepository = categoriasRepository;
        this.mapper = mapper;
    }

    // ─────────────────────────── LECTURAS ───────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<FunkoResponse> findAll(Pageable pageable) {
        return repositorio.findByDeletedFalse(pageable)
                .map(mapper::toResponse);          // Page.map: sigue siendo un Page
    }

    @Override
    @Transactional(readOnly = true)
    public FunkoResponse findById(Long id) {
        var funko = repositorio.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new FunkoNotFoundException(id));

        return mapper.toResponse(funko);
    }

    // ─────────────────────────── ESCRITURAS ─────────────────────────

    /** POST */
    @Override
    @Transactional
    public FunkoResponse save(FunkoCreateRequest request) {

        // R2 · el nombre no puede repetirse
        if (repositorio.existsByNombreIgnoreCaseAndDeletedFalse(request.nombre())) {
            throw new FunkoConflictException("Ya existe un funko llamado " + request.nombre());
        }

        // R3 · la categoria tiene que existir
        var categoria = buscarCategoria(request.categoria());

        var guardado = repositorio.save(mapper.toModel(request, categoria));

        log.info("Funko creado: {} ({})", guardado.getNombre(), guardado.getId());
        return mapper.toResponse(guardado);
    }

    /** PUT: reemplaza todos los campos */
    @Override
    @Transactional
    public FunkoResponse replace(Long id, FunkoCreateRequest request) {

        var funko = repositorio.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new FunkoNotFoundException(id));

        // el nombre solo choca si es el de OTRO funko
        if (!funko.getNombre().equalsIgnoreCase(request.nombre())
                && repositorio.existsByNombreIgnoreCaseAndDeletedFalse(request.nombre())) {
            throw new FunkoConflictException("Ya existe un funko llamado " + request.nombre());
        }

        mapper.reemplazar(funko, request, buscarCategoria(request.categoria()));

        return mapper.toResponse(funko);           // sin save(): dirty checking
    }

    /** PATCH: solo lo que llega */
    @Override
    @Transactional
    public FunkoResponse update(Long id, FunkoUpdateRequest request) {

        var funko = repositorio.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new FunkoNotFoundException(id));

        if (request.nombre() != null
                && !funko.getNombre().equalsIgnoreCase(request.nombre())
                && repositorio.existsByNombreIgnoreCaseAndDeletedFalse(request.nombre())) {
            throw new FunkoConflictException("Ya existe un funko llamado " + request.nombre());
        }

        var categoria = request.categoria() == null ? null : buscarCategoria(request.categoria());
        mapper.parchear(funko, request, categoria);

        return mapper.toResponse(funko);
    }

    /** DELETE: borrado LOGICO */
    @Override
    @Transactional
    public void deleteById(Long id) {

        var funko = repositorio.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new FunkoNotFoundException(id));

        repositorio.borradoLogico(funko.getId());
        log.info("Funko borrado (lógico): {}", id);
    }

    // ─────────────────────────── AUXILIAR ───────────────────────────

    /** Busca la categoria por NOMBRE, o lanza 400. */
    private Categoria buscarCategoria(String nombre) {
        return categoriasRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new FunkoBadRequestException(
                        "La categoría '" + nombre + "' no existe"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FunkoResponse> buscar(Optional<String> categoria,
                                      Optional<String> nombre,
                                      Optional<BigDecimal> precioMax,
                                      Pageable pageable) {

        // 1 · la condicion base: los no borrados. SIEMPRE se aplica
        Specification<Funko> spec = (root, query, cb) -> cb.isFalse(root.get("deleted"));

        // 2 · un if por filtro
        if (categoria.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.upper(root.get("categoria").get("nombre")),
                            categoria.get().toUpperCase()));
        }

        if (nombre.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("nombre")),
                            "%" + nombre.get().toLowerCase() + "%"));
        }

        if (precioMax.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("precio"), precioMax.get()));
        }

        // 3 · criterios Y paginacion, en la misma llamada
        return repositorio.findAll(spec, pageable).map(mapper::toResponse);
    }
}