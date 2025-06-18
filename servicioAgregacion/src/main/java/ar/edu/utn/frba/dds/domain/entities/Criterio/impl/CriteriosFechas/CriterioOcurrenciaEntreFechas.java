package ar.edu.utn.frba.dds.domain.entities.Criterio.impl.CriteriosFechas;


import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.time.LocalDate;


public class CriterioOcurrenciaEntreFechas extends CriterioEntreFechas {

    public CriterioOcurrenciaEntreFechas(LocalDate primeraFecha, LocalDate segundaFecha) {
        super(primeraFecha, segundaFecha);
    }

    @Override
    public LocalDate getFechaNecesaria(Hecho hecho) {
        return hecho.getFechaDeOcurrencia();
    }
}
