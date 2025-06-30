package ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso;

import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteAdapter;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator.HechoComparator;

import java.util.List;

public class ConsensoAbsoluto implements IAlgoritmoConsenso {
    @Override
    public List<Hecho> curar(List<Hecho> listaHechos, List<IFuente> listaFuentes) {
        List<Hecho> listaHechosCurados = listaHechos;

        for (IFuente fuente : listaFuentes) {
            FuenteAdapter adapter = fuente.getTipo().crearAdapter();
            List<Hecho> hechosFuente = adapter.obtenerHechos();

            if (hechosFuente.isEmpty()) {break;}

            listaHechosCurados = listaHechosCurados.stream()
                    .filter(h1 -> hechosFuente.stream()
                            .anyMatch(h2 -> HechoComparator
                                    .getInstance()
                                    .compararHechos(h1,h2)
                            )
                    )
                    .toList();

            if (listaHechosCurados.isEmpty()) {break;}
        }
        return listaHechosCurados;
    }
}