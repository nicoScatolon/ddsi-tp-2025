package ar.edu.utn.frba.dds.domain.entities.Criterio.impl.CriteriosFechas;

import ar.edu.utn.frba.dds.domain.entities.Criterio.CriterioInterfaz;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;

import java.time.LocalDate;

public abstract class CriterioEntreFechas implements CriterioInterfaz {
    private final LocalDate primeraFecha;
    private final LocalDate segundaFecha;

    public CriterioEntreFechas(LocalDate primeraFecha, LocalDate segundaFecha) {
        this.primeraFecha = primeraFecha;
        this.segundaFecha = segundaFecha;
    }

    public abstract LocalDate getFechaNecesaria(HechoBase hecho);

    @Override
    public Boolean pertenece(HechoBase hecho) {
        LocalDate fecha = this.getFechaNecesaria(hecho);
        return fecha.isAfter(primeraFecha) && fecha.isBefore(segundaFecha);
    }
}
