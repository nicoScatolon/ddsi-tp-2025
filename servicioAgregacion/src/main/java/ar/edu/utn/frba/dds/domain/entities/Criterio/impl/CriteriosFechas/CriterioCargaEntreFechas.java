package ar.edu.utn.frba.dds.domain.entities.Criterio.impl.CriteriosFechas;


import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;

import java.time.LocalDate;

public class CriterioCargaEntreFechas extends CriterioEntreFechas {

    public CriterioCargaEntreFechas(LocalDate primeraFecha, LocalDate segundaFecha) {
        super(primeraFecha, segundaFecha);
    }

    @Override
    public LocalDate getFechaNecesaria(IHecho hecho) {
        return hecho.getFechaDeModificacion().toLocalDate();
    }
}
