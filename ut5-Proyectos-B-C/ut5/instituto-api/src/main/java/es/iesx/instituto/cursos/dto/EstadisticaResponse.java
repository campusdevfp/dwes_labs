package es.iesx.instituto.cursos.dto;

/** NO viene de una entidad: lo monta el servicio desde el @Query de agregado. */
public record EstadisticaResponse(String curso, long notas, Double media) {}
