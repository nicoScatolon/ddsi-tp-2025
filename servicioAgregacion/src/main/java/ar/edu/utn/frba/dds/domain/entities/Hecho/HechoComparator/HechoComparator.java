package ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HechoComparator {
    private static final HechoComparator INSTANCE = new HechoComparator();
    @Setter @Getter
    private List<IComandComparator> listaComandos = new ArrayList<>();

    private HechoComparator() { }

    public void agregarComando(IComandComparator comando) {
        listaComandos.add(comando);
    }

    public void eliminarComando(IComandComparator comando) {
        listaComandos.remove(comando);
    }



    public static HechoComparator getInstance() {
        return INSTANCE;
    }

    public boolean compararHechos(Hecho h1, Hecho h2) {
        return listaComandos.stream().allMatch( c->c.comparar(h1, h2) );
    }
}
