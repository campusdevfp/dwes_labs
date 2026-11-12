package es.iesx.discos.artistas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "artistas")
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String nombre;

    @Column(nullable = false, length = 60)
    private String pais;

    protected Artista() {}

    public Artista(String nombre, String pais) {
        this.nombre = nombre;
        this.pais = pais;
    }

    public Long getId()       { return id; }
    public String getNombre() { return nombre; }
    public String getPais()   { return pais; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPais(String pais)     { this.pais = pais; }
}
