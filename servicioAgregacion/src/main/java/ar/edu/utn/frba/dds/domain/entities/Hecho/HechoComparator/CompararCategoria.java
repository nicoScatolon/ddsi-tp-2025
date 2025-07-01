package ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.util.Objects;

public class CompararCategoria implements IComandComparator{
    @Override
    public boolean comparar(Hecho hecho1, Hecho hecho2) {
        return Objects.equals(hecho1.getCategoria(), hecho2.getCategoria());
    }
}
