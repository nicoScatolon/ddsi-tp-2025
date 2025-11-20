package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;


import ar.edu.utn.frba.dds.domain.entities.ContenidoMultimedia.ContenidoMultimedia;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("ContenidoMultimedia")
@NoArgsConstructor

public class CriterioContenidoMultimedia extends Criterio {

    @Column(name = "tenerMultimedia", nullable = true)
    @Getter
    private Boolean tenerMultimedia;

    public CriterioContenidoMultimedia(Boolean tenerMultimedia) {
        this.tenerMultimedia = tenerMultimedia;
    }
    @Override
    public Boolean pertenece(Hecho hecho) {
        List<ContenidoMultimedia> multimedia = hecho.getContenidoMultimedia();

        if (tenerMultimedia) {
            return multimedia != null && !multimedia.isEmpty();
        } else {
            return multimedia == null || multimedia.isEmpty();
        }
    }
}
