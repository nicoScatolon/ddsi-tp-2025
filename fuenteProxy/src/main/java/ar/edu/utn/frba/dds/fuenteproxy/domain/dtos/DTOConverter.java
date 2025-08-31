package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.UbicacionOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICategoriaService;


import java.time.LocalDate;

import java.time.OffsetDateTime;




public final class DTOConverter {

    private DTOConverter() {}

    // Mapper DTO externo → DTO de salida del sistema
    public static HechoOutputDTO mapToHechoDTO(HechoExternoDTO dto, ICategoriaService categoriaService) {

        return HechoOutputDTO.builder()
                .id(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(categoriaToDTO(dto.getCategoria(), categoriaService))
                .ubicacion(new UbicacionOutputDTO(dto.getLatitud(), dto.getLongitud()))
                .fechaDeOcurrencia(dto.getFechaDeOcurrencia() == null ? null
                        : LocalDate.parse(dto.getFechaDeOcurrencia()))
                .fechaDeCarga(dto.getFechaDeCarga() == null ? null
                        : OffsetDateTime.parse(dto.getFechaDeCarga()).toLocalDateTime())
                .build();
    }



    public static CategoriaOutputDTO categoriaToDTO(String nombreCategoria, ICategoriaService categoriaService) {
        return CategoriaOutputDTO.builder()
                .id(categoriaService.obtenerIdCategoria(nombreCategoria))
                .nombre(nombreCategoria)
                .build();
    }
}
