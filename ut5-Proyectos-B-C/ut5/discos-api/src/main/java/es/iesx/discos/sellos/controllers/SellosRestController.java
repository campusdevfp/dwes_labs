package es.iesx.discos.sellos.controllers;

import es.iesx.discos.sellos.dto.SelloCreateRequest;
import es.iesx.discos.sellos.dto.SelloResponse;
import es.iesx.discos.sellos.services.SellosService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("${api.path:/api}/${api.version:v1}/sellos")
public class SellosRestController {

    private final SellosService servicio;

    public SellosRestController(SellosService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<Page<SelloResponse>> findAll(
            @RequestParam Optional<Boolean> activo,
            @PageableDefault(size = 20, sort = "nombre") Pageable pageable) {

        return ResponseEntity.ok(servicio.findAll(activo, sanear(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SelloResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.findById(id));
    }

    @PostMapping
    public ResponseEntity<SelloResponse> save(@Valid @RequestBody SelloCreateRequest dto) {
        var creado = servicio.save(dto);
        return ResponseEntity
                .created(URI.create("/api/v1/sellos/" + creado.id()))
                .body(creado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        servicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Pageable sanear(Pageable p) {
        var permitidos = Set.of("id", "nombre", "activo");
        var orden = p.getSort().stream()
                .filter(o -> permitidos.contains(o.getProperty()))
                .toList();
        return PageRequest.of(p.getPageNumber(), Math.min(p.getPageSize(), 100),
                              orden.isEmpty() ? Sort.by("nombre") : Sort.by(orden).and(Sort.by("id")));
    }
}
