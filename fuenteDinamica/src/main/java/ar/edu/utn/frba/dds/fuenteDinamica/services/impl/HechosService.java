package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
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
    public Hecho cargarHecho(HechoInputDTO hechoDTO) {
        // TODO posible verificacion de usuario
        Hecho hecho = hechoInputDTO(hechoDTO);
        hecho.setActualizar(true);
        return this.hechosRepository.save(hecho);
    }

    @Override
    public Hecho modificarHecho(HechoInputDTO hecho) {
        return null; //TODO
    }

    @Override
    public Hecho revisarHecho(HechoInputDTO hecho) {
        return null; //TODO
    }

    private Hecho hechoInputDTO(HechoInputDTO dto) {
        Hecho hecho = new Hecho();
        hecho.setTitulo(dto.getTitulo());
        //
        return hecho;
    }
}
