package ar.edu.utn.frba.dds.domain.dtos;

import ar.edu.utn.frba.dds.domain.dtos.input.*;
import ar.edu.utn.frba.dds.domain.dtos.output.E_HoraOcuPorCategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.*;
import ar.edu.utn.frba.dds.domain.entities.Estadisticas.E_HoraOcurrenciaPorCategoria;
import lombok.Getter;

import java.util.List;

@Getter
public class DTOconverter {

    // --- INPUTS --- //

    public static Hecho hechoInputDTO(HechoInputDTO hechoInputDTO) {
        return Hecho.builder()
                .id(hechoInputDTO.getId())
                .titulo(hechoInputDTO.getTitulo())
                .descripcion(hechoInputDTO.getDescripcion())
                .categoria(DTOconverter.categoriaInputDTO( hechoInputDTO.getCategoriaDTO()))
                .ubicacion(DTOconverter.ubicacionInputDTO(hechoInputDTO.getUbicacion()))
                .fechaDeOcurrencia(hechoInputDTO.getFechaDeOcurrencia())
                .fechaDeCarga(hechoInputDTO.getFechaDeCarga())
                .build();
    }

    public static Categoria categoriaInputDTO(CategoriaInputDTO categoriaInputDTO) {
        return Categoria.builder()
                .id(categoriaInputDTO.getId())
                .nombre(categoriaInputDTO.getNombre())
                .build();
    }

    public static Ubicacion ubicacionInputDTO(UbicacionInputDTO ubicacionInputDTO) {
        return Ubicacion.builder()
                .provincia(ubicacionInputDTO.getProvincia())
                .localidad(ubicacionInputDTO.getLocalidad())
                .calle(ubicacionInputDTO.getCalle())
                .numero(ubicacionInputDTO.getNumero())
                .latitud(ubicacionInputDTO.getLatitud())
                .longitud(ubicacionInputDTO.getLongitud())
                .build();
    }

    public static Coleccion coleccionInputDTO(ColeccionInputDTO coleccionInputDTO) {
        List<Hecho> hechosColeccion = coleccionInputDTO.getHechos().stream().map(DTOconverter::hechoInputDTO).toList();
        return Coleccion.builder()
                .handle(coleccionInputDTO.getHandle())
                .titulo(coleccionInputDTO.getTitulo())
                .descripcion(coleccionInputDTO.getDescripcion())
                .hechos(hechosColeccion)
                .build();
    }

    public static SolicitudEliminacion solicitudEliminacionInputDTO(SolicitudEliminacionInputDTO solicitudEliminacionInputDTO) {
        return SolicitudEliminacion.builder()
                .id(solicitudEliminacionInputDTO.getId())
                .estado(solicitudEliminacionInputDTO.getEstado())
                .fechaCreacion(solicitudEliminacionInputDTO.getFechaCreacion())
                .fechaGestion(solicitudEliminacionInputDTO.getFechaGestion())
                .build();
    }

    // --- OUTPUTS --- //

    public static E_HoraOcuPorCategoriaOutputDTO eHoraOcuPorCategoriaOutputDTO(E_HoraOcurrenciaPorCategoria estadistica) {
        return E_HoraOcuPorCategoriaOutputDTO.builder()
                .id(estadistica.getId())
                .categoria(estadistica.getCategoria())
                .horaDia(estadistica.getHoraDia())
                .cantHechosHora(estadistica.getCantHechosHora())
                .cantHechosTotales(estadistica.getCantHechosTotales())
                .fechaDeCalculo(estadistica.getFechaDeCalculo())
                .build();
    }



}
