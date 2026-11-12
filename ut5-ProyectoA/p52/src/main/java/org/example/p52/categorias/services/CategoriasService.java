package org.example.p52.categorias.services;

import org.example.p52.categorias.dto.CategoriaCreateRequest;
import org.example.p52.categorias.dto.CategoriaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoriasService {
    Page<CategoriaResponse> findAll(Pageable pageable);
    CategoriaResponse findById(Long id);
    CategoriaResponse save(CategoriaCreateRequest request);
    void deleteById(Long id);
}