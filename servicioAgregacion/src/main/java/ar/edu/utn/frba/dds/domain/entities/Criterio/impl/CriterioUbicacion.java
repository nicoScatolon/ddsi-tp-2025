package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;

import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@Entity
@DiscriminatorValue("Ubicacion")
public class CriterioUbicacion extends Criterio {


    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public CriterioUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public Boolean pertenece(Hecho hecho){
        Ubicacion nuevaUbicacion = hecho.getUbicacion();
        return Objects.equals(ubicacion.getLongitud(), nuevaUbicacion.getLongitud()) && Objects.equals(ubicacion.getLatitud(), nuevaUbicacion.getLatitud());
    }

}
