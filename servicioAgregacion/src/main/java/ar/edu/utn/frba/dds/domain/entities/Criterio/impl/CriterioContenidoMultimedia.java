package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;


import ar.edu.utn.frba.dds.domain.entities.ContenidoMultimedia.ContenidoMultimedia;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ContenidoMultimedia")
@NoArgsConstructor

public class CriterioContenidoMultimedia extends Criterio {

    @ManyToOne
    @JoinColumn(name = "contenidoMultimedia_id")
    private ContenidoMultimedia contenido;
    @Override
    public Boolean pertenece(Hecho hecho) {
        return false;
    }
}
