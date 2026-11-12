package es.iesx.discos.sellos.repositories;

import es.iesx.discos.sellos.models.Sello;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellosRepository extends JpaRepository<Sello, Long> {

    Optional<Sello> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    Page<Sello> findByActivo(boolean activo, Pageable pageable);
}
