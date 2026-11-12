package es.iesx.discos.sellos.models;

import jakarta.persistence.*;

@Entity
@Table(name = "sellos")
public class Sello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @Column(nullable = false)
    private boolean activo = true;

    protected Sello() {}

    public Sello(String nombre) {
        this.nombre = nombre;
    }

    public Long getId()       { return id; }
    public String getNombre() { return nombre; }
    public boolean isActivo() { return activo; }

    public void setNombre(String nombre)   { this.nombre = nombre; }
    public void setActivo(boolean activo)  { this.activo = activo; }
}
