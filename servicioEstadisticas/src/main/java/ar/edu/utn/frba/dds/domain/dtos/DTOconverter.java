package ar.edu.utn.frba.dds.domain.dtos;

import ar.edu.utn.frba.dds.domain.dtos.input.*;
import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.estadisticas.*;
import ar.edu.utn.frba.dds.domain.entities.*;
import ar.edu.utn.frba.dds.domain.entities.Estadisticas.*;
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
                .categoria(DTOconverter.categoriaInputDTO( hechoInputDTO.getCategoria()))
                .ubicacion(DTOconverter.ubicacionInputDTO(hechoInputDTO.getUbicacion()))
                .fechaDeOcurrencia(hechoInputDTO.getFechaDeOcurrencia())
                .fechaDeCarga(hechoInputDTO.getFechaDeCarga())
                .build();
    }

    public static Categoria categoriaInputDTO(CategoriaInputDTO categoriaInputDTO) {
                Categoria c = new Categoria();
                c.setCodigoCategoria(categoriaInputDTO.getCodigoCategoria());
                c.setNombre(categoriaInputDTO.getNombre());
                return c;
    }

    public static Ubicacion ubicacionInputDTO(UbicacionInputDTO ubicacionInputDTO) {
        return Ubicacion.builder()
                .provincia(ubicacionInputDTO.getProvincia())
                .departamento(ubicacionInputDTO.getLocalidad())
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

    public static CategoriaOutputDTO categoriaOutputDTO (Categoria categoria) {
        return CategoriaOutputDTO.builder()
                .codigoCategoria(categoria.getCodigoCategoria())
                .nombre(categoria.getNombre())
                .build();
    }

    public static ColeccionOutputDTO coleccionOutputDTO (Coleccion coleccion) {
        return ColeccionOutputDTO.builder()
                .handle(coleccion.getHandle())
                .titulo(coleccion.getTitulo())
                .build();
    }


    public static E_HoraOcuPorCategoriaOutputDTO eHoraOcuPorCategoriaOutputDTO(E_HoraOcurrenciaPorCategoria estadistica) {
        return E_HoraOcuPorCategoriaOutputDTO.builder()
                .id(estadistica.getId())
                .categoriaDTO(DTOconverter.categoriaOutputDTO(estadistica.getCategoria()))
                .horaDia(estadistica.getHoraDia())
                .cantHechosHora(estadistica.getCantHechosHora())
                .cantHechosTotales(estadistica.getCantHechosTotales())
                .fechaDeCalculo(estadistica.getFechaDeCalculo())
                .build();
    }

    public static E_MayorCategoriaOutputDTO eMayorCategoriaOutputDTO(E_MayorCategoria estadistica) {
        return E_MayorCategoriaOutputDTO.builder()
                .id(estadistica.getId())
                .categoriaDTO(DTOconverter.categoriaOutputDTO(estadistica.getCategoria()))
                .cantHechosCategoria(estadistica.getCantHechosCategoria())
                .cantHechosTotales(estadistica.getCantHechosTotales())
                .fechaDeCalculo(estadistica.getFechaDeCalculo())
                .build();
    }
    public static E_MayorProvPorCategoriaOutputDTO eMayorProvinciaPorCategoriaOutputDTO(E_MayorProvinciaPorCategoria estadistica) {
        return E_MayorProvPorCategoriaOutputDTO.builder()
                .id(estadistica.getId())
                .categoriaDTO(DTOconverter.categoriaOutputDTO(estadistica.getCategoria()))
                .provincia(estadistica.getProvincia())
                .cantHechosProvincia(estadistica.getCantHechosProvincia())
                .cantHechosTotales(estadistica.getCantHechosTotales())
                .fechaDeCalculo(estadistica.getFechaDeCalculo())
                .build();
    }

    public static E_MayorProvPorColeccionOutputDTO eMayorProvinciaPorColeccionOutputDTO(E_MayorProvinciaPorColeccion estadistica) {
        return E_MayorProvPorColeccionOutputDTO.builder()
                .id(estadistica.getId())
                .coleccionDTO(DTOconverter.coleccionOutputDTO(estadistica.getColeccion()))
                .provincia(estadistica.getProvincia())
                .cantHechosProvincia(estadistica.getCantHechosProvincia())
                .cantHechosTotales(estadistica.getCantHechosTotales())
                .fechaDeCalculo(estadistica.getFechaDeCalculo())
                .build();
    }

    public static E_SolicitudesSpamOutputDTO eSolicitudesSpamOutputDTO(E_SolicitudesSpam estadistica) {
        return E_SolicitudesSpamOutputDTO.builder()
                .id(estadistica.getId())
                .solicitudesSpam(estadistica.getSolicitudesSpam())
                .solicitudesNoSpam(estadistica.getSolicitudesNoSpam())
                .fechaDeCalculo(estadistica.getFechaDeCalculo())
                .build();
    }
}
