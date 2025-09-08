package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.UbicacionOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICategoriaService;


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
        return UbicacionOutputDTO.builder()
                .provincia(dto.getProvincia())
                .localidad(dto.getLocalidad())
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
}
