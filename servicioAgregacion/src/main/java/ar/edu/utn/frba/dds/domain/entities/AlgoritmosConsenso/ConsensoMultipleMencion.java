package ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso;

import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteAdapter;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator.HechoComparator;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConsensoMultipleMencion implements IAlgoritmoConsenso {
    private TipoAlgoritmoConsenso tipo = TipoAlgoritmoConsenso.MULTIPLEMENCION;

    @Override
    public TipoAlgoritmoConsenso getTipo() {
        return this.tipo;
    }

    @Override
    public List<Hecho> curar(List<Hecho> listaHechos, List<Fuente> listaFuentes) {
        Map<Hecho, Integer> mapHechos = new HashMap<>();
        listaHechos.forEach(listaHecho -> mapHechos.put(listaHecho, 0));

        for (IFuente fuente : listaFuentes) {
            FuenteAdapter adapter = fuente.getTipo().crearAdapter(fuente);
            List<Hecho> hechosFuente = adapter.obtenerHechos();

            List<Hecho> hechosRepetidos = mapHechos.keySet().stream()
                    .filter(h1 -> hechosFuente.stream()
                            .anyMatch(h2 -> HechoComparator
                                    .getInstance()
                                    .compararHechos(h1,h2)
                            )
                    )
                    .toList();

            hechosRepetidos.forEach(h -> mapHechos.put(h, mapHechos.get(h)+1 ));
        }

        return mapHechos.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .toList();
    }
}