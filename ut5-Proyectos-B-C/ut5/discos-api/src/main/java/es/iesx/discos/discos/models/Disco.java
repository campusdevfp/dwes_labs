package es.iesx.discos.discos.models;

import es.iesx.discos.artistas.models.Artista;
import es.iesx.discos.sellos.models.Sello;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discos")
public class Disco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false)
    private Integer anio;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    /** RELACION 1 · el artista que lo graba. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artista_id", nullable = false)
    private Artista artista;

    /** RELACION 2 · el sello que lo edita. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sello_id", nullable = false)
    private Sello sello;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    protected Disco() {}

    public Disco(String titulo, Integer anio, BigDecimal precio,
                 Artista artista, Sello sello) {
        this.titulo = titulo;
        this.anio = anio;
        this.precio = precio;
        this.artista = artista;
        this.sello = sello;
    }

    @PreUpdate
    void alActualizar() { this.updatedAt = LocalDateTime.now(); }

    public Long getId()                 { return id; }
    public String getTitulo()           { return titulo; }
    public Integer getAnio()            { return anio; }
    public BigDecimal getPrecio()       { return precio; }
    public Artista getArtista()         { return artista; }
    public Sello getSello()             { return sello; }
    public boolean isDeleted()          { return deleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setTitulo(String titulo)      { this.titulo = titulo; }
    public void setAnio(Integer anio)         { this.anio = anio; }
    public void setPrecio(BigDecimal precio)  { this.precio = precio; }
    public void setArtista(Artista artista)   { this.artista = artista; }
    public void setSello(Sello sello)         { this.sello = sello; }
    public void setDeleted(boolean borrado)   { this.deleted = borrado; }
}
