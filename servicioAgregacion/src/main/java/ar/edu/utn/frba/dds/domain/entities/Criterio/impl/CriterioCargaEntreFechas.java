package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;

import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor

public class CriterioCargaEntreFechas extends Criterio {
    @Column(name = "primeraFecha")
    private LocalDateTime primeraFecha;

    @Column(name = "segundaFecha")
    private LocalDateTime segundaFecha;

    public CriterioCargaEntreFechas(LocalDateTime primeraFecha, LocalDateTime segundaFecha) {
        this.primeraFecha = primeraFecha;
        this.segundaFecha = segundaFecha;
    }

    @Override
    public Boolean pertenece(Hecho hecho) {
        LocalDateTime fecha = hecho.getFechaDeCarga();
        return fecha.isAfter(primeraFecha) && fecha.isBefore(segundaFecha);
    }

}
