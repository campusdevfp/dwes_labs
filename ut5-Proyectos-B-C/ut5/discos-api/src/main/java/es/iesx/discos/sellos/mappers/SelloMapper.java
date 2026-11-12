package es.iesx.discos.sellos.mappers;

import es.iesx.discos.sellos.dto.SelloCreateRequest;
import es.iesx.discos.sellos.dto.SelloResponse;
import es.iesx.discos.sellos.models.Sello;
import org.springframework.stereotype.Component;

@Component
public class SelloMapper {
    public SelloResponse toResponse(Sello s, long numeroDeDiscos) {
        return new SelloResponse(s.getId(), s.getNombre(), s.isActivo(), numeroDeDiscos);
    }
    public Sello toModel(SelloCreateRequest r) {
        return new Sello(r.nombre().trim());
    }
}
