package ar.edu.utn.frba.dds.domain.entities.Hecho;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface HechoBase {
    Long getId();
    Long getOrigenId();
    Long getIdFuente();
    String getTitulo();
    String getDescripcion();
    Categoria getCategoria();
    Ubicacion getUbicacion();
    LocalDate getFechaDeOcurrencia();
    Boolean getFueEliminado();
    LocalDateTime getFechaDeCarga();


    void setId(Long id);
    void setOrigenId(Long origenId);
    void setIdFuente(Long idFuente);
    void setTitulo(String titulo);
    void setDescripcion(String descripcion);
    void setCategoria(Categoria categoria);
    void setUbicacion(Ubicacion ubicacion);
    void setFechaDeOcurrencia(LocalDate fechaDeOcurrencia);
    void setFueEliminado(Boolean fueEliminado);
    void setFechaDeCarga(LocalDateTime fechaDeCarga);
}
