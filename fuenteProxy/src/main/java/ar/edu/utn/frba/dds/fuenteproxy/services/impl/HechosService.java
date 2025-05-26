package ar.edu.utn.frba.dds.fuenteproxy.services.impl;


import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.UbicacionOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteHechosExterna;
import ar.edu.utn.frba.dds.fuenteproxy.services.IHechosService;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class HechosService implements IHechosService {
    private IFuenteHechosExterna fuenteHechosExterna;

    @Override
    public Mono<List<HechoOutputDTO>> buscarTodos() {
        return fuenteHechosExterna.buscarTodos()
                .map(lista -> lista.stream()
                        .map(this::mapToHechoDTO)
                        .toList());
    }

    @Override
    public Mono<HechoOutputDTO> buscarPorId(Long id){
        return fuenteHechosExterna.buscarPorId(id)
                .map(this::mapToHechoDTO);
    }


    private HechoOutputDTO mapToHechoDTO(HechoExternoDTO dto) {
    DateTimeFormatter fmt = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    return HechoOutputDTO.builder()
            .id(dto.getId())
            .titulo(dto.getTitulo())
            .descripcion(dto.getDescripcion())
            .categoria(new CategoriaOutputDTO(null, dto.getCategoria()))
            .ubicacion(new UbicacionOutputDTO(dto.getLatitud(), dto.getLongitud()))
            .fechaDeOcurrencia(LocalDate.parse(dto.getFechaDeOcurrencia(), fmt))
            .fechaDeCarga(LocalDateTime.parse(dto.getFechaDeCarga(), fmt))
            .build();
}
}



