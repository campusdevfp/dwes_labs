package es.iesx.discos.discos.services;

import es.iesx.discos.artistas.models.Artista;
import es.iesx.discos.artistas.repositories.ArtistasRepository;
import es.iesx.discos.discos.dto.DiscoCreateRequest;
import es.iesx.discos.discos.dto.DiscoResponse;
import es.iesx.discos.discos.dto.DiscoUpdateRequest;
import es.iesx.discos.discos.exceptions.DiscoBadRequestException;
import es.iesx.discos.discos.exceptions.DiscoConflictException;
import es.iesx.discos.discos.exceptions.DiscoNotFoundException;
import es.iesx.discos.discos.mappers.DiscoMapper;
import es.iesx.discos.discos.models.Disco;
import es.iesx.discos.discos.repositories.DiscosRepository;
import es.iesx.discos.sellos.models.Sello;
import es.iesx.discos.sellos.repositories.SellosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DiscosServiceImpl implements DiscosService {

    private static final Logger log = LoggerFactory.getLogger(DiscosServiceImpl.class);

    private final DiscosRepository repositorio;
    private final ArtistasRepository artistasRepository;
    private final SellosRepository sellosRepository;
    private final DiscoMapper mapper;

    public DiscosServiceImpl(DiscosRepository repositorio,
                             ArtistasRepository artistasRepository,
                             SellosRepository sellosRepository,
                             DiscoMapper mapper) {
        this.repositorio = repositorio;
        this.artistasRepository = artistasRepository;
        this.sellosRepository = sellosRepository;
        this.mapper = mapper;
    }

    // ─────────────────────────── LECTURAS ───────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<DiscoResponse> findAll(Pageable pageable) {
        return repositorio.findByDeletedFalse(pageable).map(mapper::toResponse);
    }

    /** Paso 9 · los cuatro filtros opcionales. */
    @Override
    @Transactional(readOnly = true)
    public Page<DiscoResponse> buscar(Optional<String> artista,
                                      Optional<String> sello,
                                      Optional<String> titulo,
                                      Optional<Integer> anioMin,
                                      Pageable pageable) {

        // 1 · base: los no borrados. SIEMPRE se aplica
        Specification<Disco> spec = (root, query, cb) -> cb.isFalse(root.get("deleted"));

        // 2 · un if por filtro. Los dos primeros NAVEGAN la relacion
        if (artista.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.upper(root.get("artista").get("nombre")),
                             artista.get().toUpperCase()));
        }

        if (sello.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.upper(root.get("sello").get("nombre")),
                             sello.get().toUpperCase()));
        }

        if (titulo.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("titulo")),
                            "%" + titulo.get().toLowerCase() + "%"));
        }

        if (anioMin.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("anio"), anioMin.get()));
        }

        // 3 · criterios Y paginacion. El @EntityGraph del repositorio trae
        //     el artista y el sello en la misma consulta
        return repositorio.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DiscoResponse findById(Long id) {
        return mapper.toResponse(repositorio.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new DiscoNotFoundException(id)));
    }

    // ─────────────────────────── ESCRITURAS ─────────────────────────

    /** POST */
    @Override
    @Transactional
    public DiscoResponse save(DiscoCreateRequest request) {

        // R3 · las DOS relaciones tienen que existir
        var artista = buscarArtista(request.artista());
        var sello   = buscarSello(request.sello());

        // R4 · el sello tiene que estar activo
        comprobarSelloActivo(sello);

        // R2 · ese artista no puede repetir titulo
        if (repositorio.existsByTituloIgnoreCaseAndArtistaIdAndDeletedFalse(
                request.titulo(), artista.getId())) {
            throw new DiscoConflictException(
                    "%s ya tiene un disco titulado %s"
                            .formatted(artista.getNombre(), request.titulo()));
        }

        var guardado = repositorio.save(mapper.toModel(request, artista, sello));

        log.info("Disco creado: {} ({})", guardado.getTitulo(), guardado.getId());
        return mapper.toResponse(guardado);
    }

    /** PUT */
    @Override
    @Transactional
    public DiscoResponse replace(Long id, DiscoCreateRequest request) {

        var disco   = buscarDisco(id);
        var artista = buscarArtista(request.artista());
        var sello   = buscarSello(request.sello());

        comprobarSelloActivo(sello);

        boolean cambiaDeTituloOArtista =
                !disco.getTitulo().equalsIgnoreCase(request.titulo())
                        || !disco.getArtista().getId().equals(artista.getId());

        if (cambiaDeTituloOArtista
                && repositorio.existsByTituloIgnoreCaseAndArtistaIdAndDeletedFalse(
                        request.titulo(), artista.getId())) {
            throw new DiscoConflictException(
                    "%s ya tiene un disco titulado %s"
                            .formatted(artista.getNombre(), request.titulo()));
        }

        mapper.reemplazar(disco, request, artista, sello);
        return mapper.toResponse(disco);           // dirty checking
    }

    /** PATCH: las dos relaciones son opcionales */
    @Override
    @Transactional
    public DiscoResponse update(Long id, DiscoUpdateRequest request) {

        var disco = buscarDisco(id);

        Artista artista = request.artista() == null ? null : buscarArtista(request.artista());
        Sello   sello   = request.sello()   == null ? null : buscarSello(request.sello());

        if (sello != null) comprobarSelloActivo(sello);

        mapper.parchear(disco, request, artista, sello);
        return mapper.toResponse(disco);
    }

    /** DELETE: borrado LOGICO */
    @Override
    @Transactional
    public void deleteById(Long id) {
        var disco = buscarDisco(id);
        repositorio.borradoLogico(disco.getId());
        log.info("Disco borrado (lógico): {}", id);
    }

    // ─────────────────────────── AUXILIARES ─────────────────────────

    private Disco buscarDisco(Long id) {
        return repositorio.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new DiscoNotFoundException(id));
    }

    private Artista buscarArtista(String nombre) {
        return artistasRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new DiscoBadRequestException(
                        "El artista '" + nombre + "' no existe"));
    }

    private Sello buscarSello(String nombre) {
        return sellosRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new DiscoBadRequestException(
                        "El sello '" + nombre + "' no existe"));
    }

    /** R4 · regla de negocio: no se publica en un sello cerrado. */
    private void comprobarSelloActivo(Sello sello) {
        if (!sello.isActivo()) {
            throw new DiscoConflictException(
                    "El sello " + sello.getNombre() + " ya no está activo");
        }
    }
}
