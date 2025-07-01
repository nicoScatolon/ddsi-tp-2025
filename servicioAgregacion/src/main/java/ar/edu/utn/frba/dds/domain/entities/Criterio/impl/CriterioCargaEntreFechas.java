package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;


import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.time.LocalDateTime;

public class CriterioCargaEntreFechas implements ICriterio {
    private final LocalDateTime primeraFecha;
    private final LocalDateTime segundaFecha;

    public CriterioCargaEntreFechas(LocalDateTime primeraFecha, LocalDateTime segundaFecha) {
        this.primeraFecha = primeraFecha;
        this.segundaFecha = segundaFecha;
    }

    @Override
    public Boolean pertenece(Hecho hecho) {
        LocalDateTime fecha = this.getFechaNecesaria(hecho);
        return fecha.isAfter(primeraFecha) && fecha.isBefore(segundaFecha);
    }

    public LocalDateTime getFechaNecesaria(Hecho hecho) {
        return hecho.getFechaDeCarga();
    }
}
