package ar.edu.utn.frba.dds.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.util.List;

public interface FuenteAdapter {
    void setFuente(IFuente fuente);
    List<Hecho> actualizarHechos(List<Hecho> hechosFuente); //pedir los hechos y actualizarlos
}
