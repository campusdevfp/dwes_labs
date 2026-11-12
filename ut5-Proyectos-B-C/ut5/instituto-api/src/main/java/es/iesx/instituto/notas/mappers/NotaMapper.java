package es.iesx.instituto.notas.mappers;

import es.iesx.instituto.alumnos.models.Alumno;
import es.iesx.instituto.notas.dto.NotaCreateRequest;
import es.iesx.instituto.notas.dto.NotaResponse;
import es.iesx.instituto.notas.models.Nota;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotaMapper {

    public NotaResponse toResponse(Nota n) {
        return new NotaResponse(n.getId(), n.getAsignatura(), n.getValor(), n.getFecha());
    }

    public List<NotaResponse> toResponse(List<Nota> notas) {
        return notas.stream().map(this::toResponse).toList();
    }

    public Nota toModel(NotaCreateRequest r, Alumno alumno) {
        return new Nota(r.asignatura().trim().toUpperCase(), r.valor(), r.fecha(), alumno);
    }
}
