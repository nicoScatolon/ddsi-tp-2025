package ar.edu.utn.frba.dds.fuenteproxy.domain.mappers;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Categoria;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Ubicacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HechoExternoMapper {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static Hecho map(HechoExternoDTO dto) {
        Hecho hecho = new Hecho();

        hecho.setId(dto.getId());
        hecho.setTitulo(dto.getTitulo());
        hecho.setDescripcion(dto.getDescripcion());

        // Mapeo de ubicación
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(dto.getLatitud());
        ubicacion.setLongitud(dto.getLongitud());

        hecho.setUbicacion(ubicacion);

        // Mapeo de categoría
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getCategoria());
        hecho.setCategoria(categoria);

        // Fechas
        hecho.setFechaDeOcurrencia(LocalDate.parse(dto.getFechaDeOcurrencia(), dateTimeFormatter));
        hecho.setFechaDeCarga(LocalDateTime.parse(dto.getFechaDeCarga(), dateTimeFormatter));

        hecho.setFueEliminado(false);

        return hecho;
    }
}
