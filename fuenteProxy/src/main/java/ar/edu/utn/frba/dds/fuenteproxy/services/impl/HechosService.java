package ar.edu.utn.frba.dds.fuenteproxy.services.impl;


import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.*;


import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.TipoFuenteProxy;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesRepositoryJPA;


import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IHechosRepositoryJPA;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICategoriaService;
import ar.edu.utn.frba.dds.fuenteproxy.services.IHechosService;
import lombok.Data;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Service
public class HechosService implements IHechosService {
    private final IFuentesRepositoryJPA fuentesRepository;
    private final ICategoriaService categoriaService;
    private final IHechosRepositoryJPA hechosRepository;


    public HechosService(IFuentesRepositoryJPA fuentesRepository, ICategoriaService categoriaService, IHechosRepositoryJPA hechosRepository) {
        this.fuentesRepository = fuentesRepository;
        this.categoriaService = categoriaService;
        this.hechosRepository = hechosRepository;
    }

    @Override
    public Mono<List<HechoOutputDTO>> buscarTodos() {
        Flux<HechoOutputDTO> desdeMetaMapa =
                Mono.fromCallable(() -> fuentesRepository.findByTipo(TipoFuenteProxy.METAMAPA))
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMapMany(Flux::fromIterable)
                        .ofType(FuenteMetaMapa.class)
                        .flatMap(FuenteMetaMapa::getHechos)
                        .flatMapIterable(list -> list)
                        .map(dto -> DTOConverter.mapHechoInputToHechoOutput(dto, categoriaService));

        Flux<HechoOutputDTO> desdeDb =
                Mono.fromCallable(hechosRepository::findAll)
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMapMany(Flux::fromIterable)
                        .map(h -> DTOConverter.mapHechoToHechoOutput(h, categoriaService));

        return Flux.concat(desdeMetaMapa, desdeDb).collectList();
    }




    @Override
    public Mono<HechoOutputDTO> buscarPorId(Long id, FuenteInputDTO fuentedto) {
        Mono<Fuente> fuenteMono = Mono.fromCallable(() -> fuentesRepository.findByNombreAndBaseUrl(fuentedto.getNombre(), fuentedto.getBaseUrl())
                                .orElseThrow(() -> new RuntimeException( "No se encontró la fuente con nombre= " + fuentedto.getNombre() +  " y baseUrl= " + fuentedto.getBaseUrl())))
                                .subscribeOn(Schedulers.boundedElastic());



        return fuenteMono.flatMap(fuente ->
                Mono.fromCallable(() -> hechosRepository.findByIdOriginalAndFuente(String.valueOf(id), fuente).orElse(null))
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(h -> h != null ? Mono.just(DTOConverter.mapHechoToHechoOutput(h, categoriaService)) : Mono.error(new RuntimeException("Hecho con ID " + id + " no encontrado para la fuente " + fuente.getNombre())))
        );

    }



    @Override
    public Mono<List<HechoOutputDTO>> buscarConFiltros(HechosFilterDTO filtros) {
        return Mono.fromCallable(() -> fuentesRepository.findByTipo(TipoFuenteProxy.METAMAPA))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .ofType(FuenteMetaMapa.class)
                .flatMap(f -> f.buscarHechos(filtros))
                .flatMapIterable(list -> list)
                .map(dto -> DTOConverter.mapHechoInputToHechoOutput(dto, categoriaService))
                .collectList();
    }


    @Override
    public Mono<Void> cargarHechosFuente(Fuente fuente) {
        if (fuente.getTipo() != TipoFuenteProxy.EXTERNA) return Mono.empty();

        return fuente.getHechos()
                .flatMapMany(Flux::fromIterable)
                .map(dto -> DTOConverter.mapToHecho(dto, fuente))
                .buffer(50)
                .doOnNext(hechosRepository::saveAll)
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }


    @Override
    public void actualizarHechosScheduler() {
        List<Fuente> externas = fuentesRepository.findByTipo(TipoFuenteProxy.EXTERNA);
        if (externas.isEmpty()) return;

        for (Fuente fuente : externas) {
            List<HechoInputDTO> apiHechos = fuente.getHechos()
                    .blockOptional()
                    .orElseGet(List::of);

            if (apiHechos.isEmpty()) continue;


            Set<String> yaEnBd = new HashSet<>(hechosRepository.findAllIdOriginalByFuente(fuente));


            int insertados = 0;
            for (HechoInputDTO dto : apiHechos) {
                String idOriginal = String.valueOf(dto.getId());
                if (yaEnBd.contains(idOriginal)) {
                    continue;
                }
                Hecho nuevo = DTOConverter.mapToHecho(dto, fuente);
                hechosRepository.save(nuevo);
                insertados++;
            }

        }
    }


    @Override
    public void eliminarHechosDeFuente(Long idFuente){
        hechosRepository.deleteAllByFuenteId(idFuente);
    }


}



