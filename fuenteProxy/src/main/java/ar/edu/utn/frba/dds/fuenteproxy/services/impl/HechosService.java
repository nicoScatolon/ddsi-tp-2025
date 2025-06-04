package ar.edu.utn.frba.dds.fuenteproxy.services.impl;


import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.UbicacionOutputDTO;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.IFuenteExterna;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.IFuenteMetaMapa;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICategoriaService;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.IFuenteHechos;
import ar.edu.utn.frba.dds.fuenteproxy.services.IHechosService;
import lombok.Data;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Data
@Service

public class HechosService implements IHechosService {
    private List<IFuenteExterna> fuentesExternas;
    private List<IFuenteMetaMapa> instanciasMetaMapa;
    private ICategoriaService categoriaService;

    public HechosService(ICategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public Void agregarFuenteExterna(IFuenteExterna fuenteExterna){
        fuentesExternas.add(fuenteExterna);
        return null;
    }

    public Void agregarInstanciaMetaMapa(IFuenteMetaMapa fuenteMetaMapa){
        instanciasMetaMapa.add(fuenteMetaMapa);
        return null;
    }


    @Override
    public Mono<List<HechoOutputDTO>> buscarTodos() {
        return Flux.concat(
                        Flux.fromIterable(fuentesExternas).flatMap(IFuenteHechos::buscarTodos),
                        Flux.fromIterable(instanciasMetaMapa).flatMap(IFuenteHechos::buscarTodos)
                )
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



