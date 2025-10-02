package ar.edu.utn.frba.dds.clienteGrafico.dtos;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DTOConverter {
    public static HechoOutputDTO convertirHechoInputDTO(HechoInputDTO hechoDTO) {
        return HechoOutputDTO.builder()
                .id(hechoDTO.getId())
                .titulo(hechoDTO.getTitulo())
                .descripcion(hechoDTO.getDescripcion())
                .categoria(DTOConverter.convertirCategoriaInputDTO(hechoDTO.getCategoria()))
                .ubicacion(DTOConverter.convertirUbicacionInputDTO(hechoDTO.getUbicacion()))
                .etiquetas(
                        hechoDTO.getEtiquetas().stream()
                                .map(DTOConverter::convertirEtiquetaInputDTO)
                                .toList()
                )
                .contenidoMultimedia(
                        hechoDTO.getContenidoMultimedia().stream()
                                .map(DTOConverter::convertirContenidoMultimediaInputDTO)
                                .toList()
                )
                .fechaDeOcurrencia(hechoDTO.getFechaDeOcurrencia())
                .fechaDeCarga(hechoDTO.getFechaDeCarga())
                .contribuyente(DTOConverter.convertirContribuyenteInputDTO(hechoDTO.getContribuyente()))
                .cargadoAninimamente(hechoDTO.getCargadoAninimamente())
                .build();
    }

    public static CategoriaOutputDTO convertirCategoriaInputDTO(CategoriaInputDTO categoriaInputDTO) {
        return CategoriaOutputDTO.builder()
                .id(categoriaInputDTO.getId())
                .nombre(categoriaInputDTO.getNombre())
                .build();
    }

    public static UbicacionOutputDTO convertirUbicacionInputDTO(UbicacionInputDTO ubicacionInputDTO) {
        return UbicacionOutputDTO.builder()
                .provincia(ubicacionInputDTO.getProvincia())
                .localidad(ubicacionInputDTO.getLocalidad())
                .calle(ubicacionInputDTO.getCalle())
                .numero(ubicacionInputDTO.getNumero())
                .longitud(ubicacionInputDTO.getLongitud())
                .latitud(ubicacionInputDTO.getLatitud())
                .id(ubicacionInputDTO.getId())
                .build();
    }

    public static EtiquetaOutputDTO convertirEtiquetaInputDTO(EtiquetaInputDTO etiquetaInputDTO) {
        return EtiquetaOutputDTO.builder()
                .id(etiquetaInputDTO.getId())
                .build();
    }

    public static ContenidoMultimediaOutputDTO convertirContenidoMultimediaInputDTO(ContenidoMultimediaInputDTO contenidoMultimediaInputDTO) {
        return ContenidoMultimediaOutputDTO.builder()
                .Id(contenidoMultimediaInputDTO.getId())
                .url(contenidoMultimediaInputDTO.getUrl())
                .descripcion(contenidoMultimediaInputDTO.getDescripcion())
                .build();
    }

    public static ContribuyenteOutputDTO convertirContribuyenteInputDTO(ContribuyenteInputDTO contribuyenteInputDTO) {
        return ContribuyenteOutputDTO.builder()
                .fechaNacimiento(contribuyenteInputDTO.getFechaNacimiento())
                .apellido(contribuyenteInputDTO.getApellido())
                .nombre(contribuyenteInputDTO.getNombre())
                .id(contribuyenteInputDTO.getId())
                .build();
    }
}
