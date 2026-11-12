package es.iesx.discos.discos.controllers;

import es.iesx.discos.discos.dto.DiscoCreateRequest;
import es.iesx.discos.discos.dto.DiscoResponse;
import es.iesx.discos.discos.dto.DiscoUpdateRequest;
import es.iesx.discos.discos.services.DiscosService;
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
@RequestMapping("${api.path:/api}/${api.version:v1}/discos")
public class DiscosRestController {

    private final DiscosService servicio;

    public DiscosRestController(DiscosService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<Page<DiscoResponse>> buscar(
            @RequestParam Optional<String> artista,
            @RequestParam Optional<String> sello,
            @RequestParam Optional<String> titulo,
            @RequestParam Optional<Integer> anioMin,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {

        return ResponseEntity.ok(
                servicio.buscar(artista, sello, titulo, anioMin, sanear(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.findById(id));
    }

    @PostMapping
    public ResponseEntity<DiscoResponse> save(@Valid @RequestBody DiscoCreateRequest dto) {
        var creado = servicio.save(dto);
        return ResponseEntity.created(URI.create("/api/v1/discos/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscoResponse> replace(
            @PathVariable Long id, @Valid @RequestBody DiscoCreateRequest dto) {
        return ResponseEntity.ok(servicio.replace(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DiscoResponse> update(
            @PathVariable Long id, @Valid @RequestBody DiscoUpdateRequest dto) {
        return ResponseEntity.ok(servicio.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        servicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Pageable sanear(Pageable p) {
        var permitidos = Set.of("id", "titulo", "anio", "precio", "createdAt");
        var orden = p.getSort().stream()
                .filter(o -> permitidos.contains(o.getProperty()))
                .toList();
        return PageRequest.of(p.getPageNumber(), Math.min(p.getPageSize(), 100),
                              orden.isEmpty() ? Sort.by("id") : Sort.by(orden).and(Sort.by("id")));
    }
}
