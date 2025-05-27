package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UsuarioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;
import ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion.ConstructorSolicitudesEliminacion;
import ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion.SolicitudEliminarHecho;
import ar.edu.utn.frba.dds.domain.repository.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.utils.DetectorSpam.IDetectorDeSpam;
import ar.edu.utn.frba.dds.services.ISolicitudesEliminacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudesEliminacionService implements ISolicitudesEliminacionService {
    private static final Logger logger = LoggerFactory.getLogger(SolicitudesEliminacionService.class);
    private final ISolicitudesEliminacionRepository repository;
    private final IDetectorDeSpam detectorDeSpam;

    public SolicitudesEliminacionService(ISolicitudesEliminacionRepository repository, IDetectorDeSpam detectorDeSpam) {
        this.repository = repository;
        this.detectorDeSpam = detectorDeSpam;
    }

    @Override
    public List<SolicitudEliminarHechoOutputDTO> findAll() {
       return this.repository
                .findAll()
                .stream()
                .map(this::solicitudEliminarHechoOutputDTO)
                .toList();
    }

    @Override
    public void crearSolicitud(HechoBase hecho, String razon, String nombre, String apellido) {
        SolicitudEliminarHecho solicitud = ConstructorSolicitudesEliminacion
                .constructorSolicitud(hecho, razon, nombre, apellido);

        if (detectorDeSpam.esSpam(razon)){ //ToDO: Si se rechaza antes de construir, no se puede guardar (soft-delete)
            solicitud.rechazarAutomaticamente();
        }

        repository.save(solicitud);
    }

    @Override
    public void rechazarSolicitud(SolicitudEliminarHechoInputDTO solicitud, UsuarioInputDTO usuarioInputDTO) {
        SolicitudEliminarHecho solicitudEliminarHecho = this.solicitudEliminarHecho(solicitud);
        solicitudEliminarHecho.serRechazada(usuarioInputDTO.getNombre(), usuarioInputDTO.getApellido());
        repository.delete(solicitudEliminarHecho);
    }

    @Override
    public void aceptarSolicitud(SolicitudEliminarHechoInputDTO solicitud, UsuarioInputDTO usuarioInputDTO) {
        SolicitudEliminarHecho solicitudEliminarHecho = this.solicitudEliminarHecho(solicitud);
        solicitudEliminarHecho.serAceptada(usuarioInputDTO.getNombre(), usuarioInputDTO.getApellido());
        repository.save(solicitudEliminarHecho);
    }

    @Override
    public SolicitudEliminarHecho findByID(Long id) {
        return repository.findById(id);
    }

    @Override
    public void logearSolicitudesEliminacionCargadas(List<SolicitudEliminarHecho> solicitudes) {
        logger.info("Solicitudes de eliminación cargadas - Cantidad: {}", solicitudes.size());
        solicitudes.forEach(solicitud ->
                logger.info(
                        "Solicitud ID: {} - Hecho ID: {} - Nombre: {} {} - Razón: {} - Fecha: {}",
                        solicitud.getId(),
                        solicitud.getHecho() != null ? solicitud.getHecho().getId() : "N/A",
                        solicitud.getNombreCreador(),
                        solicitud.getApellidoCreador(),
                        acortarTexto(solicitud.getRazonDeEliminacion()),
                        solicitud.getFechaCreacion()
                )
        );
    }

    //CONSTRUCTORES
    private SolicitudEliminarHechoOutputDTO solicitudEliminarHechoOutputDTO(SolicitudEliminarHecho solicitud) {
        return SolicitudEliminarHechoOutputDTO
                .builder()
                .hecho(DTOConverter.convertirHechoOutputDTO(solicitud.getHecho()))
                .razonDeEliminacion(solicitud.getRazonDeEliminacion())
                .nombreCreador(solicitud.getNombreCreador())
                .apellidoCreador(solicitud.getApellidoCreador())
                .fechaCreacion(solicitud.getFechaCreacion())
                .build();
    }

    private SolicitudEliminarHecho solicitudEliminarHecho(SolicitudEliminarHechoInputDTO dto) {
        return ConstructorSolicitudesEliminacion
                .constructorSolicitud(
                        dto.getHecho(),
                        dto.getRazonDeEliminacion(),
                        dto.getNombreCreador(),
                        dto.getApellidoCreador());
    }

    //Metodo para acortar strings y entren en consola.
    private String acortarTexto(String texto) {
        if (texto == null) return "";
        return texto.length() <= 60 ? texto : texto.substring(0, 60) + "...";
    }
}
