
#!/usr/bin/env bash

set -e

BASE="src/main/java/org/example/p52"
RESOURCES="src/main/resources"

# Paquetes y clases
declare -A PAQUETES=(
    [common]="GlobalExceptionHandler"

    [categorias/models]="Categoria"
    [categorias/repositories]="CategoriasRepository"
    [categorias/dto]="CategoriaResponse CategoriaCreateRequest"
    [categorias/mappers]="CategoriaMapper"
    [categorias/exceptions]="CategoriaNotFoundException CategoriaConflictException"
    [categorias/services]="CategoriasService CategoriasServiceImpl"
    [categorias/controllers]="CategoriasRestController"

    [funkos/models]="Funko"
    [funkos/repositories]="FunkosRepository"
    [funkos/dto]="FunkoResponse FunkoCreateRequest FunkoUpdateRequest"
    [funkos/mappers]="FunkoMapper"
    [funkos/exceptions]="FunkoNotFoundException FunkoConflictException FunkoBadRequestException"
    [funkos/services]="FunkosService FunkosServiceImpl"
    [funkos/controllers]="FunkosRestController"
)

echo "Creando estructura Java..."

# Crear paquetes y clases
for paquete in "${!PAQUETES[@]}"; do

    DIRECTORIO="$BASE/$paquete"
    mkdir -p "$DIRECTORIO"

    # Convertir ruta en nombre de paquete Java
    NOMBRE_PAQUETE="org.example.p52.${paquete//\//.}"

    for clase in ${PAQUETES[$paquete]}; do

        FICHERO="$DIRECTORIO/$clase.java"

        if [ ! -f "$FICHERO" ]; then

            cat > "$FICHERO" <<EOF
package $NOMBRE_PAQUETE;

public class $clase {

}
EOF

            echo "Creado: $FICHERO"
        else
            echo "Ya existe: $FICHERO"
        fi

    done
done

# Crear clase principal si no existe
mkdir -p "$BASE"

if [ ! -f "$BASE/FunkosApiApplication.java" ]; then
cat > "$BASE/FunkosApiApplication.java" <<'EOF'
package es.iesx.funkos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FunkosApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FunkosApiApplication.class, args);
    }
}
EOF
fi

# Crear recursos
mkdir -p "$RESOURCES"

for fichero in \
    application.properties \
    application-dev.properties \
    application-postgres.properties \
    application-prod.properties \
    data.sql \
    data-postgres.sql
do
    touch "$RESOURCES/$fichero"
done

echo ""
echo "Estructura del proyecto creada correctamente."
