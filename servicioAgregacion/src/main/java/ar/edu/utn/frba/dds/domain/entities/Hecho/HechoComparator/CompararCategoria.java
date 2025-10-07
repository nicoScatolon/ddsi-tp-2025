package ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CompararCategoria implements IComandComparator{
    @Override
    public boolean comparar(Hecho hecho1, Hecho hecho2) {
        return Objects.equals(hecho1.getCategoria(), hecho2.getCategoria());
    }
}
