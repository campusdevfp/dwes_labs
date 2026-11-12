package es.iesx.discos.sellos.services;

import es.iesx.discos.sellos.dto.SelloCreateRequest;
import es.iesx.discos.sellos.dto.SelloResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SellosService {
    Page<SelloResponse> findAll(Optional<Boolean> activo, Pageable pageable);
    SelloResponse findById(Long id);
    SelloResponse save(SelloCreateRequest request);
    void deleteById(Long id);
}
