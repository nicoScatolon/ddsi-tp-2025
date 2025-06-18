package ar.edu.utn.frba.dds.domain.entities.Criterio.impl.CriteriosFechas;

import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.time.LocalDate;

public abstract class CriterioEntreFechas implements ICriterio {
    private final LocalDate primeraFecha;
    private final LocalDate segundaFecha;

    public CriterioEntreFechas(LocalDate primeraFecha, LocalDate segundaFecha) {
        this.primeraFecha = primeraFecha;
        this.segundaFecha = segundaFecha;
    }

    public abstract LocalDate getFechaNecesaria(Hecho hecho);

    @Override
    public Boolean pertenece(Hecho hecho) {
        LocalDate fecha = this.getFechaNecesaria(hecho);
        return fecha.isAfter(primeraFecha) && fecha.isBefore(segundaFecha);
    }
}
