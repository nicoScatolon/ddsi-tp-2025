package ar.edu.utn.frba.dds.fuenteproxy.services.impl;


import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.*;


import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesSelector;


import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad.ServidoraDeHechos;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICategoriaService;
import ar.edu.utn.frba.dds.fuenteproxy.services.IHechosService;
import lombok.Data;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.List;


@Data
@Service
public class HechosService implements IHechosService {
    private final IFuentesSelector fuentesRepository;
    private final ICategoriaService categoriaService;

    public HechosService(IFuentesSelector fuentesRepository, ICategoriaService categoriaService) {
        this.fuentesRepository = fuentesRepository;
        this.categoriaService = categoriaService;
    }

    @Override
    public Mono<List<HechoOutputDTO>> buscarTodos() {
        return Flux.fromIterable(fuentesRepository.fuentesConHechos())
                .flatMap(ServidoraDeHechos::getHechos)
                .flatMapIterable(lista -> lista)
                .map(dto -> DTOConverter.mapToHechoDTO(dto, categoriaService))
                .collectList();
    }

    @Override
    public Mono<HechoOutputDTO> buscarPorId(Long id) {
        return Flux.fromIterable(fuentesRepository.fuentesQuePermitenBuscarPorId())
                .flatMap(fuente -> fuente.getHechoPorId(id)
                        .onErrorResume(e -> Mono.empty()))
                .next()
                .map(dto -> DTOConverter.mapToHechoDTO(dto, categoriaService))
                .switchIfEmpty(Mono.error(new RuntimeException("Hecho con ID " + id + " no encontrado en ninguna fuente")));
    }


    @Override
    public Mono<List<HechoOutputDTO>> buscarConFiltros(HechosFilterDTO filtros) {
        return Flux.fromIterable(fuentesRepository.fuentesConFiltros())
                .flatMap(f -> f.buscarHechos(filtros))
                .flatMapIterable(lista -> lista)
                .map(dto -> DTOConverter.mapToHechoDTO(dto, categoriaService))
                .collectList();
    }
}



