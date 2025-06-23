package ar.edu.utn.frba.dds.fuenteproxy.services.impl;


import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.UbicacionOutputDTO;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad.RegistroDeFuentes;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad.ServidoraDeColecciones;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad.ServidoraDeHechos;
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
public class HechosService  {
    private final RegistroDeFuentes registro;
    private final ICategoriaService categoriaService;

    public HechosService(RegistroDeFuentes registro, ICategoriaService categoriaService) {
        this.registro = registro;
        this.categoriaService = categoriaService;
    }


    public Mono<List<HechoOutputDTO>> buscarTodos() {
        return Flux.fromIterable(registro.todasLasFuentes())
                .flatMap(ServidoraDeHechos::getHechos)
                .flatMapIterable(lista -> lista)
                .map(this::mapToHechoDTO)
                .collectList();
    }


    public Mono<HechoOutputDTO> buscarPorId(Long id) {
        return Flux.fromIterable(registro.fuentesQuePermitenBuscarPorId())
                .flatMap(fuente -> fuente.getHechoPorId(id)
                        .onErrorResume(e -> Mono.empty()))
                .next()
                .map(this::mapToHechoDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Hecho con ID " + id + " no encontrado en ninguna fuente")));
    }



    public Mono<List<HechoOutputDTO>> buscarConFiltros(String categoria, String frDesde, String frHasta,
                                                       String faDesde, String faHasta, String ubicacion) {
        return Flux.fromIterable(registro.fuentesConFiltros())
                .flatMap(f -> f.buscarConFiltros(categoria, frDesde, frHasta, faDesde, faHasta, ubicacion))
                .flatMapIterable(lista -> lista)
                .map(this::mapToHechoDTO)
                .collectList();
    }




    public Mono<List<ColeccionInputDTO>> traerTodasLasColecciones() {
        return Flux.fromIterable(registro.fuentesConColecciones())
                .flatMap(ServidoraDeColecciones::buscarTodasLasColecciones)
                .flatMapIterable(lista -> lista)
                .collectList();
    }





    public Mono<List<HechoOutputDTO>> traerHechosDeColeccion(String idColeccion) {
        return Flux.fromIterable(registro.fuentesConColecciones())
                .flatMap(f -> f.buscarPorColeccion(idColeccion))
                .flatMapIterable(lista -> lista)
                .map(this::mapToHechoDTO)
                .collectList();
    }





    public Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoOutputDTO solicitud) {
        return Flux.fromIterable(registro.fuentesQuePermitenEliminar())
                .next()
                .flatMap(f -> f.crearSolicitudEliminacion(solicitud))
                .switchIfEmpty(Mono.error(new RuntimeException("No hay fuentes que permitan crear solicitudes de eliminación")));
    }






    // Mapper DTO externo → DTO de salida del sistema
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

    private CategoriaOutputDTO categoriaToDTO(String nombreCategoria) {
        return CategoriaOutputDTO.builder()
                .id(categoriaService.obtenerIdCategoria(nombreCategoria))
                .nombre(nombreCategoria)
                .build();
    }

}



