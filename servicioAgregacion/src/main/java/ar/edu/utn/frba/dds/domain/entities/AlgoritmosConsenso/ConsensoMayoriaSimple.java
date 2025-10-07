package ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso;

import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteAdapter;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator.HechoComparator;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConsensoMayoriaSimple implements IAlgoritmoConsenso {
    private TipoAlgoritmoConsenso tipo = TipoAlgoritmoConsenso.MAYORIASIMPLE;

    @Override
    public TipoAlgoritmoConsenso getTipo() {
        return this.tipo;
    }

    @Override
    public List<Hecho> curar(List<Hecho> listaHechos, List<Fuente> listaFuentes) {
        Map<Hecho, Integer> mapHechos = new HashMap<>();
        listaHechos.forEach(listaHecho -> mapHechos.put(listaHecho, 0));
        Integer cantApariciones = (listaHechos.size()+1)/2;

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
                .filter(entry -> entry.getValue() >= cantApariciones)
                .map(Map.Entry::getKey)
                .toList();
    }
}
