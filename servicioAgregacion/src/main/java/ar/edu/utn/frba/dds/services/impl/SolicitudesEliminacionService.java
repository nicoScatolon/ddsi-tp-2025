package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.ProcesarSolicitudInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.ConstructorSolicitudesEliminacion;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.EstadoDeSolicitud;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;
import ar.edu.utn.frba.dds.domain.repository.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.services.IFuentesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.utils.DetectorSpam.IDetectorDeSpam;
import ar.edu.utn.frba.dds.services.ISolicitudesEliminacionService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudesEliminacionService implements ISolicitudesEliminacionService {
    private static final Logger logger = LoggerFactory.getLogger(SolicitudesEliminacionService.class);
    private final ISolicitudesEliminacionRepository repository;
    private final IHechosService hechosService;
    private final IDetectorDeSpam detectorDeSpam;

    public SolicitudesEliminacionService(ISolicitudesEliminacionRepository repository, IDetectorDeSpam detectorDeSpam, IHechosService hechosService) {
        this.repository = repository;
        this.detectorDeSpam = detectorDeSpam;
        this.hechosService = hechosService;
    }

    @Override
    public List<SolicitudEliminarHechoOutputDTO> findAll(Long idCreador) {
        if (idCreador == null) {
            return this.repository
                    .findAll()
                    .stream()
                    .map(DTOConverter::solicitudEliminarHechoOutputDTO)
                    .toList();
        }
        else {
            return this.repository
                    .findAllByIdCreador(idCreador)
                    .stream()
                    .map(DTOConverter::solicitudEliminarHechoOutputDTO)
                    .toList();
        }
    }

    @Override
    public List<SolicitudEliminarHechoOutputDTO> findSinProcesar(){
        return this.repository
                .findByEstado(EstadoDeSolicitud.PENDIENTE)
                .stream()
                .map(DTOConverter::solicitudEliminarHechoOutputDTO)
                .toList();
    }

    @Override
    public ResponseEntity<Void>  crearSolicitudDesdeEntidad(Hecho hecho, String razon, Long idCreador) {
        SolicitudEliminarHecho solicitud = ConstructorSolicitudesEliminacion
                .constructorSolicitud(hecho, razon, idCreador);
        if (detectorDeSpam.esSpam(razon)){
            solicitud.rechazarSpam();
        }

        repository.save(solicitud);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> crearSolicitudDesdeDTO(SolicitudEliminarHechoInputDTO solicitud){
        Hecho hecho = hechosService.findEntidadPorId(solicitud.getHechoId());

        if (hecho == null || hechosService.findByID(hecho.getId()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hecho no encontrado");
        }
        return  this.crearSolicitudDesdeEntidad(
                hecho,
                solicitud.getRazonDeEliminacion(),
                solicitud.getIdCreador()
        );
    }


    @Override
    public ResponseEntity<Void> procesarSolicitud(ProcesarSolicitudInputDTO procesarSolicitudDTO, Boolean aceptar) {
        Hecho hecho = hechosService.findEntidadPorId(procesarSolicitudDTO.getSolicitud().getHechoId());
        if (hecho == null) {
            return ResponseEntity.notFound().build();
        }

        SolicitudEliminarHecho solicitud = repository.findById(procesarSolicitudDTO.getSolicitud().getIdSolicitud())
                .orElse(null);

        if (solicitud == null) {
            solicitud = DTOConverter.solicitudEliminarHecho(procesarSolicitudDTO.getSolicitud(), hecho);
        }

        if (procesarSolicitudDTO.getAdministradorId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (aceptar) {
            solicitud.serAceptada(procesarSolicitudDTO.getAdministradorId());
            repository.save(solicitud);
            hechosService.guardarHecho(solicitud.getHecho());
            this.rechazarRestoSolicitudes(procesarSolicitudDTO.getSolicitud().getIdSolicitud(), hecho.getId(), procesarSolicitudDTO.getAdministradorId());
        } else {
            solicitud.serRechazada(procesarSolicitudDTO.getAdministradorId());
            repository.save(solicitud);
        }

        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> notificarHechoEliminado(List<Hecho> hechosFuente) {
        repository.deleteByHechoIn(hechosFuente);
        return ResponseEntity.ok().build();
    }


    private void rechazarRestoSolicitudes(Long idSolicitud, Long idHecho, Long idAdmin){
        List<SolicitudEliminarHecho> solicitudesARechazar = repository
                .findAllByHechoIdExcludingSolicitud(idHecho, idSolicitud);

        solicitudesARechazar.forEach(s -> {
            s.serRechazada(idAdmin);
            repository.save(s);
        });
    }
    @Override
    public SolicitudEliminarHecho findByID(Long id) {
        return repository.getById(id);
    }

    @Override
    public void logearSolicitudesEliminacionCargadas(List<SolicitudEliminarHecho> solicitudes) {
        logger.info("Solicitudes de eliminación cargadas - Cantidad: {}", solicitudes.size());
        solicitudes.forEach(solicitud ->
                logger.info(
                        "Solicitud ID: {} - Hecho ID: {} - Admin ID: {} - Razón: {} - Fecha: {}",
                        solicitud.getId(),
                        solicitud.getHecho() != null ? solicitud.getHecho().getId() : "N/A",
                        solicitud.getIdAdministrador(),
                        acortarTexto(solicitud.getRazonDeEliminacion()),
                        solicitud.getFechaCreacion()
                )
        );
    }


    //Metodo para acortar strings y entren en consola.
    private String acortarTexto(String texto) {
        if (texto == null) return "";
        return texto.length() <= 60 ? texto : texto.substring(0, 60) + "...";
    }
}