package org.example.p52.categorias.services;

import org.example.p52.categorias.dto.CategoriaCreateRequest;
import org.example.p52.categorias.dto.CategoriaResponse;
import org.example.p52.categorias.exceptions.CategoriaConflictException;
import org.example.p52.categorias.exceptions.CategoriaNotFoundException;
import org.example.p52.categorias.mappers.CategoriaMapper;
import org.example.p52.categorias.repositories.CategoriasRepository;
import org.example.p52.funkos.repositories.FunkosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriasServiceImpl implements CategoriasService {

    private final CategoriasRepository repositorio;
    private final FunkosRepository funkosRepository;
    private final CategoriaMapper mapper;

    public CategoriasServiceImpl(CategoriasRepository repositorio,
                                 FunkosRepository funkosRepository,
                                 CategoriaMapper mapper) {
        this.repositorio = repositorio;
        this.funkosRepository = funkosRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaResponse> findAll(Pageable pageable) {
        return repositorio.findAll(pageable)
                .map(c -> mapper.toResponse(
                        c, funkosRepository.countByCategoriaIdAndDeletedFalse(c.getId())));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse findById(Long id) {
        var categoria = repositorio.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException(id));

        return mapper.toResponse(
                categoria, funkosRepository.countByCategoriaIdAndDeletedFalse(id));
    }

    @Override
    @Transactional
    public CategoriaResponse save(CategoriaCreateRequest request) {
        if (repositorio.existsByNombreIgnoreCase(request.nombre())) {
            throw new CategoriaConflictException(
                    "Ya existe la categoría " + request.nombre());
        }
        return mapper.toResponse(repositorio.save(mapper.toModel(request)), 0);
    }

    /** R4 · una categoria con funkos NO se borra */
    @Override
    @Transactional
    public void deleteById(Long id) {
        var categoria = repositorio.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException(id));

        long funkos = funkosRepository.countByCategoriaIdAndDeletedFalse(id);
        if (funkos > 0) {
            throw new CategoriaConflictException(
                    "No se puede borrar la categoría %s: tiene %d funkos"
                            .formatted(categoria.getNombre(), funkos));
        }

        repositorio.deleteById(id);
    }
}