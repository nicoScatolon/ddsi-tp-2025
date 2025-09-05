package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechosFilterDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad.ServidoraDeColecciones;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesSelector;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICategoriaService;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICollecionesService;
import lombok.Data;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
@Service
public class ColeccionesService implements ICollecionesService {
    private final IFuentesSelector fuentesRepository;
    private final ICategoriaService categoriaService;

    public ColeccionesService(IFuentesSelector fuentesRepository, ICategoriaService categoriaService) {
        this.fuentesRepository = fuentesRepository;
        this.categoriaService = categoriaService;
    }

    @Override
    public Mono<List<ColeccionInputDTO>> traerTodasLasColecciones() {
        return Flux.fromIterable(fuentesRepository.fuentesConColecciones())
                .flatMap(ServidoraDeColecciones::buscarTodasLasColecciones)
                .flatMapIterable(lista -> lista)
                .collectList();
    }




    @Override
    public Mono<List<HechoOutputDTO>> traerHechosDeColeccion(String handleColeccion) {
        HechosFilterDTO filtros = new HechosFilterDTO();
        return Flux.fromIterable(fuentesRepository.fuentesConColecciones())
                .flatMap(f -> f.buscarHechosPorColeccion(handleColeccion, false,filtros))
                .flatMapIterable(lista -> lista)
                .map(dto -> DTOConverter.mapToHechoDTO(dto, categoriaService))
                .collectList();
    }

}
