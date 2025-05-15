package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.repository.ISolicitudesRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.ISolicitudesService;

public class SolicitudesService implements ISolicitudesService {
    private ISolicitudesRepository solicitudesRepository;

    public SolicitudesService(ISolicitudesRepository solicitudesRepository) {
        this.solicitudesRepository = solicitudesRepository;
    }
}
