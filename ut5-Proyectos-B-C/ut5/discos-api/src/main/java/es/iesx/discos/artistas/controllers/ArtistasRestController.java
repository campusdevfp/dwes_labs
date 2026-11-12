package es.iesx.discos.artistas.controllers;

import es.iesx.discos.artistas.dto.ArtistaCreateRequest;
import es.iesx.discos.artistas.dto.ArtistaResponse;
import es.iesx.discos.artistas.services.ArtistasService;
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
@RequestMapping("${api.path:/api}/${api.version:v1}/artistas")
public class ArtistasRestController {

    private final ArtistasService servicio;

    public ArtistasRestController(ArtistasService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<Page<ArtistaResponse>> findAll(
            @RequestParam Optional<String> pais,
            @PageableDefault(size = 20, sort = "nombre") Pageable pageable) {

        return ResponseEntity.ok(servicio.findAll(pais, sanear(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.findById(id));
    }

    @PostMapping
    public ResponseEntity<ArtistaResponse> save(@Valid @RequestBody ArtistaCreateRequest dto) {
        var creado = servicio.save(dto);
        return ResponseEntity
                .created(URI.create("/api/v1/artistas/" + creado.id()))
                .body(creado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        servicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Pageable sanear(Pageable p) {
        var permitidos = Set.of("id", "nombre", "pais");
        var orden = p.getSort().stream()
                .filter(o -> permitidos.contains(o.getProperty()))
                .toList();
        return PageRequest.of(p.getPageNumber(), Math.min(p.getPageSize(), 100),
                              orden.isEmpty() ? Sort.by("nombre") : Sort.by(orden).and(Sort.by("id")));
    }
}
