package ar.edu.utn.frba.dds.domain.entities.Geolocalizadores;

import ar.edu.utn.frba.dds.domain.entities.Ubicacion;

public interface IGeoLocalizador {
    Ubicacion geolocalizar(Ubicacion ubicacion);

}
