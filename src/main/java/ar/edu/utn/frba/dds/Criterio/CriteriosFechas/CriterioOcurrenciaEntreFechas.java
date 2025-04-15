package ar.edu.utn.frba.dds.Criterio.CriteriosFechas;

import ar.edu.utn.frba.dds.Hechos.Hecho;
import java.time.LocalDateTime;

public class CriterioOcurrenciaEntreFechas extends CriterioEntreFechas {

    public CriterioOcurrenciaEntreFechas(LocalDateTime primeraFecha, LocalDateTime segundaFecha) {
        super(primeraFecha, segundaFecha);
    }

    @Override
    public LocalDateTime getFechaNecesaria(Hecho hecho) {
        return hecho.getFechaDeCarga();
    }
}
