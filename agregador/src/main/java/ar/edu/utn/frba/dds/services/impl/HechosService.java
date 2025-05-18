package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HechosService implements IHechosService {
    @Autowired //ToDO Se puede reemplazar (Es lo mas recomendable)
    private IHechosRepository repository;


    public List<HechoOutputDTO> buscarTodosLosHechos(){
        //ToDO, si es Admin, aquí se debería verificar
        return this.repository
                .findAll()
                .stream()
                .map(this::hechoOutputDTO)
                .toList();
    }

    private HechoOutputDTO hechoOutputDTO(Hecho hecho) {
        return HechoOutputDTO.builder()
                .id(hecho.getId())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoria(hecho.getCategoria())
                .ubicacion(hecho.getUbicacion())
                .fechaDeOcurrencia(hecho.getFechaDeOcurrencia())
                .build();
    }
}

//Min 1.37.50