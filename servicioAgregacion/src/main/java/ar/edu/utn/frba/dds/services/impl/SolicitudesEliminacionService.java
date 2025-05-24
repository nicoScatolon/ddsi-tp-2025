package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion.ConstructorSolicitudesEliminacion;
import ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion.SolicitudEliminarHecho;
import ar.edu.utn.frba.dds.domain.repository.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.services.ISolicitudesEliminacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudesEliminacionService implements ISolicitudesEliminacionService {
    private static final Logger logger = LoggerFactory.getLogger(SolicitudesEliminacionService.class);
    private final ISolicitudesEliminacionRepository repository;

    public SolicitudesEliminacionService(ISolicitudesEliminacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SolicitudEliminarHechoOutputDTO> findAll() {
        //ToDO, si es Admin, aquí se debería verificar
        return this.repository
                .findAll()
                .stream()
                .map(this::solicitudEliminarHechoOutputDTO)
                .toList();
    }

    @Override
    public void crearSolicitud(IHecho hecho, String razon, String nombre, String apellido) {
        SolicitudEliminarHecho solicitud = ConstructorSolicitudesEliminacion
                .constructorSolicitud(hecho, razon, nombre, apellido);

        repository.save(solicitud);
    }


    @Override
    public List<SolicitudEliminarHechoOutputDTO> recolectarSolicitudes(String fuenteURL) {
        //ToDo
        return List.of();
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
                .hecho(solicitud.getHecho())
                .razonDeEliminacion(solicitud.getRazonDeEliminacion())
                .nombreCreador(solicitud.getNombreCreador())
                .apellidoCreador(solicitud.getApellidoCreador())
                .fechaCreacion(solicitud.getFechaCreacion())
                .build();
    }

    // Método auxiliar para acortar texto en logs
    private String acortarTexto(String texto) {
        if (texto == null) return "";
        return texto.length() <= 60 ? texto : texto.substring(0, 60) + "...";
    }
}
