package ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso;

import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteAdapter;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator.HechoComparator;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue(("ABSOLUTO"))

public class ConsensoAbsoluto extends AlgoritmoConsenso {
    private TipoAlgoritmoConsenso tipo = TipoAlgoritmoConsenso.ABSOLUTO;

    @Override
    public TipoAlgoritmoConsenso getTipo() {
        return this.tipo;
    }

    @Override
    public List<Hecho> curar(List<Hecho> listaHechos, List<Fuente> listaFuentes) {
        List<Hecho> listaHechosCurados = listaHechos;

        for (IFuente fuente : listaFuentes) {
            FuenteAdapter adapter = fuente.getTipo().crearAdapter(fuente);
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