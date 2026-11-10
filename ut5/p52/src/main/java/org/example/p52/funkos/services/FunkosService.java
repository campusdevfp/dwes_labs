package org.example.p52.funkos.services;

import org.example.p52.funkos.dto.FunkoCreateRequest;
import org.example.p52.funkos.dto.FunkoResponse;
import org.example.p52.funkos.dto.FunkoUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FunkosService {
    Page<FunkoResponse> findAll(Pageable pageable);
    FunkoResponse findById(Long id);
    FunkoResponse save(FunkoCreateRequest request);
    FunkoResponse replace(Long id, FunkoCreateRequest request);
    FunkoResponse update(Long id, FunkoUpdateRequest request);
    void deleteById(Long id);
}