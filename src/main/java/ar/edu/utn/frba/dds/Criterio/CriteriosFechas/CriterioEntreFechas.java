package ar.edu.utn.frba.dds.Criterio.CriteriosFechas;

import ar.edu.utn.frba.dds.Criterio.Criterio;
import ar.edu.utn.frba.dds.Hechos.Hecho;
import java.time.LocalDateTime;

public abstract class CriterioEntreFechas implements Criterio {
    private LocalDateTime primeraFecha;
    private LocalDateTime segundaFecha;

    public CriterioEntreFechas(LocalDateTime primeraFecha, LocalDateTime segundaFecha) {
        this.primeraFecha = primeraFecha;
        this.segundaFecha = segundaFecha;
    }

    public abstract LocalDateTime getFechaNecesaria(Hecho hecho);

    @Override
    public boolean pertenece(Hecho hecho) {
        LocalDateTime fecha = this.getFechaNecesaria(hecho);
        return fecha.isAfter(primeraFecha) && fecha.isBefore(segundaFecha);
    }
}
