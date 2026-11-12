package es.iesx.discos.sellos.services;

import es.iesx.discos.sellos.dto.SelloCreateRequest;
import es.iesx.discos.sellos.dto.SelloResponse;
import es.iesx.discos.sellos.exceptions.SelloConflictException;
import es.iesx.discos.sellos.exceptions.SelloNotFoundException;
import es.iesx.discos.sellos.mappers.SelloMapper;
import es.iesx.discos.sellos.repositories.SellosRepository;
import es.iesx.discos.discos.repositories.DiscosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SellosServiceImpl implements SellosService {

    private final SellosRepository repositorio;
    private final DiscosRepository discosRepository;
    private final SelloMapper mapper;

    public SellosServiceImpl(SellosRepository repositorio,
                             DiscosRepository discosRepository,
                             SelloMapper mapper) {
        this.repositorio = repositorio;
        this.discosRepository = discosRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SelloResponse> findAll(Optional<Boolean> activo, Pageable pageable) {
        var pagina = activo.isPresent()
                ? repositorio.findByActivo(activo.get(), pageable)
                : repositorio.findAll(pageable);

        return pagina.map(s -> mapper.toResponse(
                s, discosRepository.countBySelloIdAndDeletedFalse(s.getId())));
    }

    @Override
    @Transactional(readOnly = true)
    public SelloResponse findById(Long id) {
        var sello = repositorio.findById(id)
                .orElseThrow(() -> new SelloNotFoundException(id));

        return mapper.toResponse(sello, discosRepository.countBySelloIdAndDeletedFalse(id));
    }

    @Override
    @Transactional
    public SelloResponse save(SelloCreateRequest request) {
        if (repositorio.existsByNombreIgnoreCase(request.nombre())) {
            throw new SelloConflictException("Ya existe el sello " + request.nombre());
        }
        return mapper.toResponse(repositorio.save(mapper.toModel(request)), 0);
    }

    /** R5 · un sello con discos NO se borra. */
    @Override
    @Transactional
    public void deleteById(Long id) {
        var sello = repositorio.findById(id)
                .orElseThrow(() -> new SelloNotFoundException(id));

        long discos = discosRepository.countBySelloIdAndDeletedFalse(id);
        if (discos > 0) {
            throw new SelloConflictException(
                    "No se puede borrar %s: tiene %d discos"
                            .formatted(sello.getNombre(), discos));
        }

        repositorio.deleteById(id);
    }
}
