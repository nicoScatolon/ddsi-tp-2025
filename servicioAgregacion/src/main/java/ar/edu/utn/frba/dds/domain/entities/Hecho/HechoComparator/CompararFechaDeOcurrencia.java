package ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.time.LocalDate;

public class CompararFechaDeOcurrencia implements IComandComparator{
    @Override
    public boolean comparar(Hecho hecho1, Hecho hecho2) {
        if (hecho1 == null || hecho2 == null || hecho1.getFechaDeOcurrencia() == null || hecho2.getFechaDeOcurrencia() == null) {
            return false;
        }

        LocalDate f1 = hecho1.getFechaDeOcurrencia();
        LocalDate f2 = hecho2.getFechaDeOcurrencia();

        long diasDeDiferencia = Math.abs(f1.toEpochDay() - f2.toEpochDay());
        return diasDeDiferencia <= 1;
    }
}
