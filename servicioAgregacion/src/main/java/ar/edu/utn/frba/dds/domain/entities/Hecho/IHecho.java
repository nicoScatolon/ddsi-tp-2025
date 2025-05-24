package ar.edu.utn.frba.dds.domain.entities.Hecho;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IHecho {
    Long getId();
    Long getFuenteId();
    String getTitulo();
    String getDescripcion();
    Categoria getCategoria();
    Ubicacion getUbicacion();
    LocalDate getFechaDeOcurrencia();
    LocalDateTime getFechaDeModificacion();
    Boolean getFueEliminado();


    void setId(Long id);
    void setFuenteId(Long fuenteId);
    void setTitulo(String titulo);
    void setDescripcion(String descripcion);
    void setCategoria(Categoria categoria);
    void setUbicacion(Ubicacion ubicacion);
    void setFechaDeOcurrencia(LocalDate fechaDeOcurrencia);
    void setFechaDeModificacion(LocalDateTime fechaDeModificacion);
    void setFueEliminado(Boolean fueEliminado);
}
