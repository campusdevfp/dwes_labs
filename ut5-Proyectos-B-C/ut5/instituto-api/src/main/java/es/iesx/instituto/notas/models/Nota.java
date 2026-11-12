package es.iesx.instituto.notas.models;

import es.iesx.instituto.alumnos.models.Alumno;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notas")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String asignatura;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private LocalDate fecha;

    /** ESLABON 2 de la cadena. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    protected Nota() {}

    public Nota(String asignatura, Double valor, LocalDate fecha, Alumno alumno) {
        this.asignatura = asignatura;
        this.valor = valor;
        this.fecha = fecha;
        this.alumno = alumno;
    }

    public Long getId()          { return id; }
    public String getAsignatura(){ return asignatura; }
    public Double getValor()     { return valor; }
    public LocalDate getFecha()  { return fecha; }
    public Alumno getAlumno()    { return alumno; }

    public void setAsignatura(String asignatura) { this.asignatura = asignatura; }
    public void setValor(Double valor)           { this.valor = valor; }
}
