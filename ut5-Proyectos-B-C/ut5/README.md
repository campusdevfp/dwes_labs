# Soluciones · UT5 · proyectos paso a paso B y C

Los proyectos **terminados** de la página *4. Tres proyectos paso a paso* del alumno. Cada clase es la del paso correspondiente, en el paquete que marca el árbol del paso 1.

| Carpeta | Proyecto | Clases | Base de datos |
|---|---|:-:|---|
| `discos-api/` | **B · Discos**: dos 1:N sobre la misma entidad (artista y sello) | 34 | `discos` |
| `instituto-api/` | **C · Instituto**: cadena curso → alumno → nota | 30 | `instituto` |

Los dos usan **Spring Boot 3.5.6** y Java 25, como el `curl` del paso 1. Si los pasas a Spring Boot 4, cambia el import de `PropertyReferenceException` en `common/GlobalExceptionHandler.java` (`org.springframework.data.core`).

## Arrancar

```bash
cd discos-api                 # o instituto-api
mvn spring-boot:run           # H2 en fichero, perfil dev
```

```bash
curl -s "localhost:8080/api/v1/discos?size=3" | jq '.totalElements'      # 7
curl -s "localhost:8080/api/v1/alumnos?curso=2DAW" | jq '[.content[].apellidos]'
```

Con PostgreSQL:

```bash
docker compose up -d
SPRING_PROFILES_ACTIVE=postgres mvn spring-boot:run
```

!!! warning "Base de datos limpia"
    El perfil `dev` usa H2 **en fichero** (`./datos/`) con `ddl-auto=update`: los borrados lógicos y las altas se quedan entre arranques. Para volver a los datos de `data.sql`, borra la carpeta `datos/`.

## Qué mirar si el de un alumno no funciona

| Síntoma | Dónde está la diferencia |
|---|---|
| `Not a managed type` al arrancar | Una entidad en otro paquete o sin `@Entity` |
| `LazyInitializationException` | Falta `@Transactional` en el servicio, o es el de `jakarta.transaction` |
| 41 consultas en `GET /discos?size=20` | Falta `"sello"` (o los dos) en el `@EntityGraph` |
| El contador de matriculados no cuadra (C) | El `@Modifying` se llama **antes** de los setters |
| `/cursos/estadisticas` da 400 (C) | El `@GetMapping("/estadisticas")` está **después** de `/{id}` |
