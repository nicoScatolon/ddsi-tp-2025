package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;

import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity

@NoArgsConstructor

public class CriterioOcurrenciaEntreFechas extends Criterio {
    private  LocalDate primeraFecha;
    private  LocalDate segundaFecha;

    public CriterioOcurrenciaEntreFechas(LocalDate primeraFecha, LocalDate segundaFecha) {
        this.primeraFecha = primeraFecha;
        this.segundaFecha = segundaFecha;
    }

    @Override
    public Boolean pertenece(Hecho hecho) {
        LocalDate fecha = hecho.getFechaDeOcurrencia();
        return fecha.isAfter(primeraFecha) && fecha.isBefore(segundaFecha);
    }

}
