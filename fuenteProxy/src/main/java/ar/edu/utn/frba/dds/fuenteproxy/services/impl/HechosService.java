package ar.edu.utn.frba.dds.fuenteproxy.services.impl;


import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.UbicacionOutputDTO;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.adapters.IFuenteAdapter;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICategoriaService;
import ar.edu.utn.frba.dds.fuenteproxy.services.IHechosService;
import lombok.Data;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Data
@Service

public class HechosService implements IHechosService {
    private final List<IFuenteAdapter> fuentesAdapters = new ArrayList<>();
    private final ICategoriaService categoriaService;

    public HechosService(ICategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public void agregarFuente(IFuenteAdapter fuenteAdapter) {
        fuentesAdapters.add(fuenteAdapter);
    }

    @Override
    public Mono<List<HechoOutputDTO>> buscarTodos() {
        return Flux.fromIterable(fuentesAdapters)
                .flatMap(IFuenteAdapter::obtenerHechos)
                .flatMapIterable(lista -> lista)
                .map(this::mapToHechoDTO)
                .collectList();
    }



    private HechoOutputDTO mapToHechoDTO(HechoExternoDTO dto) {
    DateTimeFormatter fmt = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    return HechoOutputDTO.builder()
            .id(dto.getId())
            .titulo(dto.getTitulo())
            .descripcion(dto.getDescripcion())
            .categoria(categoriaToDTO(dto.getCategoria()))
            .ubicacion(new UbicacionOutputDTO(dto.getLatitud(), dto.getLongitud()))
            .fechaDeOcurrencia(LocalDate.parse(dto.getFechaDeOcurrencia(), fmt))
            .fechaDeCarga(LocalDateTime.parse(dto.getFechaDeCarga(), fmt))
            .build();
}



private CategoriaOutputDTO categoriaToDTO(String nombreCategoria){
        return CategoriaOutputDTO.builder()
                .id(categoriaService.obtenerIdCategoria(nombreCategoria))
                .nombre(nombreCategoria)
                .build();
}



}



