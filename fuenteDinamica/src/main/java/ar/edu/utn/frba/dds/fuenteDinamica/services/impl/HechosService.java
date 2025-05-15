package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repository.IHechosRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IHechosService;
import org.springframework.stereotype.Service;

@Service
public class HechosService implements IHechosService {
    private IHechosRepository hechosRepository;

    public HechosService(IHechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    @Override
    public Hecho cargarHecho(Hecho hecho) {
        //TODO verificar como es la conexion que nos trae el hecho, para saber si viene de una API
        // posiblemente reciba un input DTO por la interfaz que se debe transformar en la entidad serie
        return this.hechosRepository.save(hecho);
    }

    @Override
    public Hecho modificarHecho(Hecho hecho) {
        return null; //TODO
    }

    @Override
    public Hecho revisarHecho(Hecho hecho) {
        return null; //TODO
    }
}
