package es.iesx.instituto.cursos.controllers;

import es.iesx.instituto.cursos.dto.CursoCreateRequest;
import es.iesx.instituto.cursos.dto.CursoResponse;
import es.iesx.instituto.cursos.dto.EstadisticaResponse;
import es.iesx.instituto.cursos.services.CursosService;
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
import java.util.Set;

@RestController
@RequestMapping("${api.path:/api}/${api.version:v1}/cursos")
public class CursosRestController {

    private final CursosService servicio;

    public CursosRestController(CursosService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<Page<CursoResponse>> findAll(
            @PageableDefault(size = 20, sort = "codigo") Pageable pageable) {
        return ResponseEntity.ok(servicio.findAll(sanear(pageable)));
    }

    /** OJO: va ANTES que /{id}, o Spring intenta convertir "estadisticas" en Long. */
    @GetMapping("/estadisticas")
    public ResponseEntity<List<EstadisticaResponse>> estadisticas() {
        return ResponseEntity.ok(servicio.estadisticas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.findById(id));
    }

    @PostMapping
    public ResponseEntity<CursoResponse> save(@Valid @RequestBody CursoCreateRequest dto) {
        var creado = servicio.save(dto);
        return ResponseEntity
                .created(URI.create("/api/v1/cursos/" + creado.id()))
                .body(creado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        servicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Pageable sanear(Pageable p) {
        var permitidos = Set.of("id", "codigo", "nombre", "plazas", "matriculados");
        var orden = p.getSort().stream()
                .filter(o -> permitidos.contains(o.getProperty()))
                .toList();
        return PageRequest.of(p.getPageNumber(), Math.min(p.getPageSize(), 100),
                              orden.isEmpty() ? Sort.by("codigo") : Sort.by(orden).and(Sort.by("id")));
    }
}
