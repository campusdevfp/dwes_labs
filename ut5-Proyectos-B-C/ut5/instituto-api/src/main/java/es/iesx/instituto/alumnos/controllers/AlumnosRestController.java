package es.iesx.instituto.alumnos.controllers;

import es.iesx.instituto.alumnos.dto.AlumnoCreateRequest;
import es.iesx.instituto.alumnos.dto.AlumnoResponse;
import es.iesx.instituto.alumnos.dto.AlumnoUpdateRequest;
import es.iesx.instituto.alumnos.services.AlumnosService;
import es.iesx.instituto.notas.dto.NotaCreateRequest;
import es.iesx.instituto.notas.dto.NotaResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("${api.path:/api}/${api.version:v1}/alumnos")
public class AlumnosRestController {

    private final AlumnosService servicio;

    public AlumnosRestController(AlumnosService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<Page<AlumnoResponse>> buscar(
            @RequestParam Optional<String> curso,
            @RequestParam Optional<String> apellidos,
            @RequestParam Optional<Double> notaMin,
            @PageableDefault(size = 20, sort = "apellidos") Pageable pageable) {

        return ResponseEntity.ok(
                servicio.buscar(curso, apellidos, notaMin, sanear(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.findById(id));
    }

    @PostMapping
    public ResponseEntity<AlumnoResponse> save(@Valid @RequestBody AlumnoCreateRequest dto) {
        var creado = servicio.save(dto);
        return ResponseEntity
                .created(URI.create("/api/v1/alumnos/" + creado.id()))
                .body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlumnoResponse> replace(
            @PathVariable Long id, @Valid @RequestBody AlumnoCreateRequest dto) {
        return ResponseEntity.ok(servicio.replace(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AlumnoResponse> update(
            @PathVariable Long id, @Valid @RequestBody AlumnoUpdateRequest dto) {
        return ResponseEntity.ok(servicio.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        servicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ───────── las notas, colgando del alumno ─────────

    @GetMapping("/{id}/notas")
    public ResponseEntity<List<NotaResponse>> notas(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.notasDe(id));
    }

    @PostMapping("/{id}/notas")
    public ResponseEntity<NotaResponse> anadirNota(
            @PathVariable Long id, @Valid @RequestBody NotaCreateRequest dto) {

        var creada = servicio.anadirNota(id, dto);
        return ResponseEntity
                .created(URI.create("/api/v1/alumnos/" + id + "/notas/" + creada.id()))
                .body(creada);
    }

    private Pageable sanear(Pageable p) {
        var permitidos = Set.of("id", "expediente", "nombre", "apellidos", "createdAt");
        var orden = p.getSort().stream()
                .filter(o -> permitidos.contains(o.getProperty()))
                .toList();
        return PageRequest.of(p.getPageNumber(), Math.min(p.getPageSize(), 100),
                              orden.isEmpty() ? Sort.by("apellidos") : Sort.by(orden).and(Sort.by("id")));
    }
}
