package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Usuario;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.solicitudEliminacion.ConstructorSolicitudesEliminacion;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repository.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.ISolicitudesEliminacionService;

public class SolicitudesEliminacionService implements ISolicitudesEliminacionService {
    private ISolicitudesEliminacionRepository solicitudesRepository;

    public SolicitudesEliminacionService(ISolicitudesEliminacionRepository solicitudesRepository) {
        this.solicitudesRepository = solicitudesRepository;
    }

    @Override
    public SolicitudEliminacion crearSolicitud(SolicitudEliminacion solicitudDTO) {
        //SolicitudEliminacion nuevaSolicitud = ConstructorSolicitudesEliminacion.constructorSolicitud(1,1,1);
        //solicitudesRepository.save(nuevaSolicitud);
        //TODO
        return null;
    }

    @Override
    public SolicitudEliminacion aceptarSolicitud(SolicitudEliminacion solicitudDTO, Usuario adminDTO) {
        //TODO
        return null;
    }

    public SolicitudEliminacion rechazarSolicitud(SolicitudEliminacion solicitudDTO, Usuario adminDTO) {
        //TODO
        return null;
    }
}
