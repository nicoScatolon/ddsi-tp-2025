package ar.edu.utn.frba.dds.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class HechoFilter {
    private String categoria;
    private LocalDateTime fReporteDesde;

    private LocalDateTime fReporteHasta;

    private LocalDate fAconDesde;

    private LocalDate fAconHasta;

    private Double latitud;
    private Double longitud;
}
