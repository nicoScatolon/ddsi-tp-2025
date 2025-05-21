package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion.SolicitudEliminarHecho;
import ar.edu.utn.frba.dds.domain.repository.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.services.ISolicitudesEliminacionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudesEliminacionService implements ISolicitudesEliminacionService {
    private final ISolicitudesEliminacionRepository repository;

    public SolicitudesEliminacionService(ISolicitudesEliminacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SolicitudEliminarHechoOutputDTO> buscarTodasLasSolicitudes() {
        //ToDO, si es Admin, aquí se debería verificar
        return this.repository
                .findAll()
                .stream()
                .map(this::solicitudEliminarHechoOutputDTO)
                .toList();
    }

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
}
