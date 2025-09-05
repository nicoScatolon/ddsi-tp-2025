package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesSelector;
import ar.edu.utn.frba.dds.fuenteproxy.services.ISolicitudesEliminacionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SolicitudesEliminacionService implements ISolicitudesEliminacionService {
    private final IFuentesSelector fuentesRepository;

    public SolicitudesEliminacionService(IFuentesSelector fuentesRepository) {
        this.fuentesRepository = fuentesRepository;
    }

    @Override
    public Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoOutputDTO solicitud) {
        return Flux.fromIterable(fuentesRepository.fuentesQuePermitenEliminar())
                .next()
                .switchIfEmpty(Mono.error(new RuntimeException("No hay fuentes que permitan crear solicitudes de eliminación")))
                .flatMap(f -> f.crearSolicitudEliminacion(solicitud));

    }
}
