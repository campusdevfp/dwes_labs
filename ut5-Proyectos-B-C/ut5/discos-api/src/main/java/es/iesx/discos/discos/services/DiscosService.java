package es.iesx.discos.discos.services;

import es.iesx.discos.discos.dto.DiscoCreateRequest;
import es.iesx.discos.discos.dto.DiscoResponse;
import es.iesx.discos.discos.dto.DiscoUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DiscosService {

    Page<DiscoResponse> findAll(Pageable pageable);

    /** El listado con los cuatro filtros opcionales (paso 8). */
    Page<DiscoResponse> buscar(Optional<String> artista,
                               Optional<String> sello,
                               Optional<String> titulo,
                               Optional<Integer> anioMin,
                               Pageable pageable);

    DiscoResponse findById(Long id);
    DiscoResponse save(DiscoCreateRequest request);
    DiscoResponse replace(Long id, DiscoCreateRequest request);
    DiscoResponse update(Long id, DiscoUpdateRequest request);
    void deleteById(Long id);
}
