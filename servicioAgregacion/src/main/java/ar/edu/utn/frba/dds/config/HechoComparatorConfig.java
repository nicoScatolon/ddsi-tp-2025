package ar.edu.utn.frba.dds.config;

import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator.HechoComparator;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator.IComandComparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.util.List;

@Configuration
public class HechoComparatorConfig {

    @Value("#{'${app.hechos.comparadores}'.split(',')}")
    private List<String> comparadoresActivos;

    private final List<IComandComparator> todosLosComparadores;

    public HechoComparatorConfig(List<IComandComparator> todosLosComparadores) {
        this.todosLosComparadores = todosLosComparadores;
    }

    @PostConstruct
    public void inicializarComparador() {
        HechoComparator comparator = HechoComparator.getInstance();

        for (IComandComparator c : todosLosComparadores) {
            String nombre = c.getClass().getSimpleName()
                    .replace("Comparar", "")
                    .toLowerCase();

            if (comparadoresActivos.contains(nombre)) {
                comparator.agregarComando(c);
            }
        }

        System.out.println("✅ HechoComparator inicializado con: " +
                comparator.getListaComandos().stream()
                        .map(cmd -> cmd.getClass().getSimpleName())
                        .toList());
    }
}