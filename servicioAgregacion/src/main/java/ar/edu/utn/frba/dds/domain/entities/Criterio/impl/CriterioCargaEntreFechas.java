package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@DiscriminatorValue("CargaEntreFechas")
@NoArgsConstructor
@Getter
public class CriterioCargaEntreFechas extends Criterio {
    @Column(name = "primeraFecha", nullable = true)
    private LocalDateTime primeraFecha;

    @Column(name = "segundaFecha", nullable = true)
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
