package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.TipoFuenteProxy;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesRepositoryJPA;
import ar.edu.utn.frba.dds.fuenteproxy.services.ISolicitudesEliminacionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean esVisualizador =  auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("VISUALIZADOR"));
        if(esVisualizador && solicitud.getRazonDeEliminacion().length() < 500){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"Los visualizadores deben ingresar una justificación de al menos 500 caracteres"
            );
        }
        return Mono.fromCallable(() -> fuentesRepository.findByTipo(TipoFuenteProxy.METAMAPA))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .ofType(FuenteMetaMapa.class)
                .concatMap(f -> f.crearSolicitudEliminacion(solicitud).onErrorResume(e -> Mono.empty()))
                .then();
    }

}
