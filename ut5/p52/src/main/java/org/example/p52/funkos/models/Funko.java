package org.example.p52.funkos.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.p52.categorias.models.Categoria;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "funkos")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Funko {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "categoria_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_funko_categoria")
    )
    private Categoria categoria;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Funko(String nombre, BigDecimal precio,
                 Integer cantidad, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.categoria = categoria;
    }

    @PreUpdate
    void alActualizar() {
        this.updatedAt = LocalDateTime.now();
    }
}