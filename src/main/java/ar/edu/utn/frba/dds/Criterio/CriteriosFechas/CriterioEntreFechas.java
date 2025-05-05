package ar.edu.utn.frba.dds.Criterio.CriteriosFechas;

import ar.edu.utn.frba.dds.Criterio.CriterioInterfaz;
import ar.edu.utn.frba.dds.Hechos.Hecho;
import java.time.LocalDate;

public abstract class CriterioEntreFechas implements CriterioInterfaz {
    private LocalDate primeraFecha;
    private LocalDate segundaFecha;

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
