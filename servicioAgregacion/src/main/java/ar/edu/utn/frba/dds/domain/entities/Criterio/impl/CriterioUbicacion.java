package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;

import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;

import java.util.Objects;

public class CriterioUbicacion implements ICriterio {
    private Ubicacion ubicacion;

    public CriterioUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public Boolean pertenece(Hecho hecho){
        Ubicacion nuevaUbicacion = hecho.getUbicacion();
        return Objects.equals(ubicacion.getLongitud(), nuevaUbicacion.getLongitud()) && Objects.equals(ubicacion.getLatitud(), nuevaUbicacion.getLatitud());
    }

}
