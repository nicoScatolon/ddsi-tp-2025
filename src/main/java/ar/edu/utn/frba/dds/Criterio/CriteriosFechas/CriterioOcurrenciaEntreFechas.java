package ar.edu.utn.frba.dds.Criterio.CriteriosFechas;

import ar.edu.utn.frba.dds.Hechos.Hecho;
import java.time.LocalDate;

public class CriterioOcurrenciaEntreFechas extends CriterioEntreFechas {

    public CriterioOcurrenciaEntreFechas(LocalDate primeraFecha, LocalDate segundaFecha) {
        super(primeraFecha, segundaFecha);
    }

    @Override
    public LocalDate getFechaNecesaria(Hecho hecho) {
        return hecho.getFechaDeCarga();
    }
}
