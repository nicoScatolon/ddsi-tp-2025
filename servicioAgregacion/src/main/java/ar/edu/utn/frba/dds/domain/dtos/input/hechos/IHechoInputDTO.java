package ar.edu.utn.frba.dds.domain.dtos.input.hechos;

import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IHechoInputDTO {
        Long getId();
        String getTitulo();
        String getDescripcion();
        String getNombreCategoria();
        UbicacionInputDTO getUbicacion();
        LocalDate getFechaDeOcurrencia();
        LocalDateTime getFechaDeCarga();
}
