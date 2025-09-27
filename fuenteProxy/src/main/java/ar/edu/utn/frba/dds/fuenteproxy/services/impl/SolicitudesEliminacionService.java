package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesRepositoryJPA;
import ar.edu.utn.frba.dds.fuenteproxy.services.ISolicitudesEliminacionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class SolicitudesEliminacionService implements ISolicitudesEliminacionService {
    private final IFuentesRepositoryJPA fuentesRepository;

    public SolicitudesEliminacionService(IFuentesRepositoryJPA fuentesRepository) {
        this.fuentesRepository = fuentesRepository;
    }

    @Override
    public Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoInputDTO solicitud) {
        return Mono.fromCallable(fuentesRepository::findAllMetaMapa)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .concatMap(f -> f.crearSolicitudEliminacion(solicitud).onErrorResume(e -> Mono.empty()))
                .then();
    }
}
