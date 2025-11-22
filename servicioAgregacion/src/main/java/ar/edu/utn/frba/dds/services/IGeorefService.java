package ar.edu.utn.frba.dds.services;

import java.util.Map;

public interface IGeorefService {
    void inicializarCache();
    String buscarProvincia(String nombreProvincia);
    Map<String, String> buscarJurisdiccionPorCoordenadas(Double lat, Double lon);
}
