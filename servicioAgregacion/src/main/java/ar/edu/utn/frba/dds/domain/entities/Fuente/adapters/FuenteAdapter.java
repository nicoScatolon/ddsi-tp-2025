package ar.edu.utn.frba.dds.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.util.List;

public interface FuenteAdapter {
    void setFuente(IFuente fuente);
    List<Hecho> obtenerHechosFuente(); //pedir los hechos y actualizarlos
    List<Hecho> obtenerHechosCargados(); //pedir los que estan cargados
}
