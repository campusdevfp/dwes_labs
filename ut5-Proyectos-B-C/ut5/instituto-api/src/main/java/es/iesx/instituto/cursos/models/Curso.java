package es.iesx.instituto.cursos.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false)
    private Integer plazas;

    /** Contador: lo lleva una @Query, no un setter. */
    @Column(nullable = false)
    private Integer matriculados = 0;

    protected Curso() {}

    public Curso(String codigo, String nombre, Integer plazas) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.plazas = plazas;
    }

    public Long getId()             { return id; }
    public String getCodigo()       { return codigo; }
    public String getNombre()       { return nombre; }
    public Integer getPlazas()      { return plazas; }
    public Integer getMatriculados(){ return matriculados; }

    public void setNombre(String nombre)   { this.nombre = nombre; }
    public void setPlazas(Integer plazas)  { this.plazas = plazas; }
}
