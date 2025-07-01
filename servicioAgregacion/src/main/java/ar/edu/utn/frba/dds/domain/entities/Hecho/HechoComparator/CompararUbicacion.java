package ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;

import java.util.Objects;

public class CompararUbicacion implements IComandComparator{
    @Override
    public boolean comparar(Hecho hecho1, Hecho hecho2) {
        Ubicacion ubicacion1 = hecho1.getUbicacion();
        Ubicacion ubicacion2 = hecho2.getUbicacion();
        return (Objects.equals(ubicacion1.getLongitud(), ubicacion2.getLongitud()) && (Objects.equals(ubicacion1.getLatitud(), ubicacion2.getLatitud())));
    }
}
