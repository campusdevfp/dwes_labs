package es.iesx.instituto.alumnos.mappers;

import es.iesx.instituto.alumnos.dto.AlumnoCreateRequest;
import es.iesx.instituto.alumnos.dto.AlumnoResponse;
import es.iesx.instituto.alumnos.dto.AlumnoUpdateRequest;
import es.iesx.instituto.alumnos.models.Alumno;
import es.iesx.instituto.cursos.models.Curso;
import org.springframework.stereotype.Component;

@Component
public class AlumnoMapper {

    /** La nota media llega calculada: la pide el servicio al repositorio de notas. */
    public AlumnoResponse toResponse(Alumno a, Double notaMedia) {
        return new AlumnoResponse(
                a.getId(), a.getExpediente(), a.getNombre(), a.getApellidos(),
                a.getCurso().getCodigo(), a.getCurso().getNombre(),
                notaMedia, a.isActivo());
    }

    public Alumno toModel(AlumnoCreateRequest r, Curso curso) {
        return new Alumno(r.expediente().trim().toUpperCase(),
                          r.nombre().trim(), r.apellidos().trim(), curso);
    }

    public void reemplazar(Alumno alumno, AlumnoCreateRequest r, Curso curso) {
        alumno.setNombre(r.nombre().trim());
        alumno.setApellidos(r.apellidos().trim());
        alumno.setCurso(curso);
    }

    public void parchear(Alumno alumno, AlumnoUpdateRequest r, Curso curso) {
        if (r.nombre()    != null) alumno.setNombre(r.nombre().trim());
        if (r.apellidos() != null) alumno.setApellidos(r.apellidos().trim());
        if (curso         != null) alumno.setCurso(curso);
    }
}
