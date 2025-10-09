package ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso;

import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.util.List;

public interface IAlgoritmoConsenso {
    TipoAlgoritmoConsenso getTipo();
    List<Hecho> curar(List<Hecho> listaHechos, List< List<Hecho> > listaHechosFuentes);
}
