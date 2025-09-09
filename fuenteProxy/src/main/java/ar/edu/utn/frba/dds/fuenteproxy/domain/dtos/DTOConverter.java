package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.UbicacionOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.FuenteDDS;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.FuenteMetaMapa;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICategoriaService;
import ar.edu.utn.frba.dds.fuenteproxy.services.impl.FuenteFactory;


import java.time.LocalDate;

import java.time.OffsetDateTime;




public final class DTOConverter {

    private DTOConverter() {}

    // Mapper DTO externo → DTO de salida del sistema
    public static HechoOutputDTO mapToHechoDTO(HechoInputDTO dto, ICategoriaService categoriaService) {

        return HechoOutputDTO.builder()
                .id(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(categoriaToDTO(dto.getCategoria(), categoriaService))
                .ubicacion(mapToUbicacionDTO(dto.getUbicacion()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia() == null ? null
                        : LocalDate.parse(dto.getFechaDeOcurrencia()))
                .fechaDeCarga(dto.getFechaDeCarga() == null ? null
                        : OffsetDateTime.parse(dto.getFechaDeCarga()).toLocalDateTime())
                .build();
    }


    public static UbicacionOutputDTO mapToUbicacionDTO(UbicacionInputDTO dto){
        if(dto == null) return null;
        return UbicacionOutputDTO.builder()
                .provincia(dto.getProvincia())
                .departamento(dto.getDepartamento())
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .latitud(dto.getLatitud())
                .longitud(dto.getLongitud())
                .build();
    }



    public static CategoriaOutputDTO categoriaToDTO(String nombreCategoria, ICategoriaService categoriaService) {
        return CategoriaOutputDTO.builder()
                .id(categoriaService.obtenerIdCategoria(nombreCategoria))
                .nombre(nombreCategoria)
                .build();
    }

    public static FuenteMetaMapa mapToFuenteMetaMapa(FuenteInputDTO dto, FuenteFactory factory) {
        return factory.nuevaFuenteMetaMapa(dto.getNombre(), dto.getBaseUrl());
    }

    public static FuenteDDS mapToFuenteDDS(FuenteInputDTO dto, FuenteFactory factory) {
        return factory.nuevaFuenteDDS(dto.getNombre());
    }

    public static FuenteOutputDTO mapToFuenteOutputDTO(FuenteMetaMapa fuente) {
        return FuenteOutputDTO.builder()
                .id(fuente.getId())
                .nombre(fuente.getNombre())
                .tipo(fuente.getTipo().name())
                .build();
    }

    public static FuenteOutputDTO mapToFuenteOutputDTO(FuenteDDS fuente) {
        return FuenteOutputDTO.builder()
                .id(fuente.getId())
                .nombre(fuente.getNombre())
                .tipo(fuente.getTipo().name())
                .build();
    }
}
