package org.example.p52.categorias.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.p52.funkos.models.Funko;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @OneToMany(mappedBy = "categoria")
    private List<Funko> funkos = new ArrayList<>();

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}



