package es.iesx.instituto.alumnos.models;

import es.iesx.instituto.cursos.models.Curso;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alumnos")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 12)
    private String expediente;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 120)
    private String apellidos;

    /** ESLABON 1 de la cadena. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    protected Alumno() {}

    public Alumno(String expediente, String nombre, String apellidos, Curso curso) {
        this.expediente = expediente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.curso = curso;
    }

    @PreUpdate
    void alActualizar() { this.updatedAt = LocalDateTime.now(); }

    public Long getId()                 { return id; }
    public String getExpediente()       { return expediente; }
    public String getNombre()           { return nombre; }
    public String getApellidos()        { return apellidos; }
    public Curso getCurso()             { return curso; }
    public boolean isActivo()           { return activo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setNombre(String nombre)        { this.nombre = nombre; }
    public void setApellidos(String apellidos)  { this.apellidos = apellidos; }
    public void setCurso(Curso curso)           { this.curso = curso; }
    public void setActivo(boolean activo)       { this.activo = activo; }
}
