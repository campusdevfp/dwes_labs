package es.iesx.discos.artistas.services;

import es.iesx.discos.artistas.dto.ArtistaCreateRequest;
import es.iesx.discos.artistas.dto.ArtistaResponse;
import es.iesx.discos.artistas.exceptions.ArtistaConflictException;
import es.iesx.discos.artistas.exceptions.ArtistaNotFoundException;
import es.iesx.discos.artistas.mappers.ArtistaMapper;
import es.iesx.discos.artistas.repositories.ArtistasRepository;
import es.iesx.discos.discos.repositories.DiscosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ArtistasServiceImpl implements ArtistasService {

    private final ArtistasRepository repositorio;
    private final DiscosRepository discosRepository;
    private final ArtistaMapper mapper;

    public ArtistasServiceImpl(ArtistasRepository repositorio,
                               DiscosRepository discosRepository,
                               ArtistaMapper mapper) {
        this.repositorio = repositorio;
        this.discosRepository = discosRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtistaResponse> findAll(Optional<String> pais, Pageable pageable) {
        var pagina = pais.isPresent()
                ? repositorio.findByPaisIgnoreCase(pais.get(), pageable)
                : repositorio.findAll(pageable);

        return pagina.map(a -> mapper.toResponse(
                a, discosRepository.countByArtistaIdAndDeletedFalse(a.getId())));
    }

    @Override
    @Transactional(readOnly = true)
    public ArtistaResponse findById(Long id) {
        var artista = repositorio.findById(id)
                .orElseThrow(() -> new ArtistaNotFoundException(id));

        return mapper.toResponse(
                artista, discosRepository.countByArtistaIdAndDeletedFalse(id));
    }

    @Override
    @Transactional
    public ArtistaResponse save(ArtistaCreateRequest request) {
        if (repositorio.existsByNombreIgnoreCase(request.nombre())) {
            throw new ArtistaConflictException("Ya existe el artista " + request.nombre());
        }
        return mapper.toResponse(repositorio.save(mapper.toModel(request)), 0);
    }

    /** R5 · un artista con discos NO se borra. */
    @Override
    @Transactional
    public void deleteById(Long id) {
        var artista = repositorio.findById(id)
                .orElseThrow(() -> new ArtistaNotFoundException(id));

        long discos = discosRepository.countByArtistaIdAndDeletedFalse(id);
        if (discos > 0) {
            throw new ArtistaConflictException(
                    "No se puede borrar a %s: tiene %d discos"
                            .formatted(artista.getNombre(), discos));
        }

        repositorio.deleteById(id);
    }
}
