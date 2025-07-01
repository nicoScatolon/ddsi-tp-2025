package ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso;

import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator.HechoComparator;

import java.util.List;

public interface IAlgoritmoConsenso {
    TipoAlgoritmoConsenso getTipo();
    List<Hecho> curar(List<Hecho> listaHechos, List<IFuente> listaFuentes);
}
