package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechosFilterDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.TipoFuenteProxy;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesRepositoryJPA;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICategoriaService;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICollecionesService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColeccionesService implements ICollecionesService {

    private final IFuentesRepositoryJPA fuentesRepository;
    private final ICategoriaService categoriaService;

    @Override
    public Mono<List<ColeccionInputDTO>> traerTodasLasColecciones() {
        return Flux.defer(() -> Flux.fromIterable(
                        fuentesRepository.findByTipo(TipoFuenteProxy.METAMAPA)
                ))
                .subscribeOn(Schedulers.boundedElastic())
                .ofType(FuenteMetaMapa.class)
                .flatMap(FuenteMetaMapa::buscarTodasLasColecciones)
                .flatMapIterable(list -> list)
                .collectList();
    }



    @Override
    public Mono<List<HechoOutputDTO>> traerHechosDeColeccion(String handleColeccion) {
        HechosFilterDTO filtros = new HechosFilterDTO();

        return Flux.defer(() -> Flux.fromIterable(
                        fuentesRepository.findByTipo(TipoFuenteProxy.METAMAPA)
                ))
                .subscribeOn(Schedulers.boundedElastic())
                .ofType(FuenteMetaMapa.class)
                .concatMap(f -> f.buscarHechosPorColeccion(handleColeccion, false, filtros))
                .flatMapIterable(list -> list)
                .map(dto -> DTOConverter.mapHechoInputToHechoOutput(dto, categoriaService))
                .collectList();
    }

}

