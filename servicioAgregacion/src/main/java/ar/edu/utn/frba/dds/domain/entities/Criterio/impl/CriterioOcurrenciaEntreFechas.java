package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;


import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.time.LocalDate;


public class CriterioOcurrenciaEntreFechas implements ICriterio {
    private final LocalDate primeraFecha;
    private final LocalDate segundaFecha;

    public CriterioOcurrenciaEntreFechas(LocalDate primeraFecha, LocalDate segundaFecha) {
        this.primeraFecha = primeraFecha;
        this.segundaFecha = segundaFecha;
    }

    @Override
    public Boolean pertenece(Hecho hecho) {
        LocalDate fecha = this.getFechaNecesaria(hecho);
        return fecha.isAfter(primeraFecha) && fecha.isBefore(segundaFecha);
    }

    public LocalDate getFechaNecesaria(Hecho hecho) {
        return hecho.getFechaDeOcurrencia();
    }
}
