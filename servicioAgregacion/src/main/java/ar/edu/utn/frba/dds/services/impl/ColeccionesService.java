package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import ar.edu.utn.frba.dds.domain.repository.impl.ColeccionesRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.HechosRepository;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ColeccionesService implements IColeccionesService {
    private final ColeccionesRepository coleccionesRepository;
    private final HechosRepository hechosRepository;

    public ColeccionesService(ColeccionesRepository coleccionesRepository, HechosRepository hechosRepository) {
        this.coleccionesRepository = coleccionesRepository;
        this.hechosRepository = hechosRepository;
    }

    @Override
    public ColeccionOutputDTO findByHandle(String handle) {
        return this.coleccionOutputDTO(coleccionesRepository.findByHandle(handle));
    }

    @Override
    public List<ColeccionOutputDTO> findAll() {
        return coleccionesRepository.findAll().stream()
                .map(this::coleccionOutputDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ColeccionOutputDTO crearColeccion(ColeccionInputDTO coleccionInputDTO) {
        var coleccion = new Coleccion(
                coleccionInputDTO.getHandle(),
                coleccionInputDTO.getTitulo(),
                coleccionInputDTO.getDescripcion());

        coleccionInputDTO.getListaCriterios().forEach(coleccion::agregarCriterio);

        return this.coleccionOutputDTO(coleccion);
    }

    @Override
    public List<HechoOutputDTO> hechosDeLaColeccionByHandle(String handle) {
        return coleccionesRepository.hechosByHandle(handle,hechosRepository.findAll()).stream()
                .map(MapperHechos::convertirHechoOutputDTO)
                .collect(Collectors.toList());
    }

    private ColeccionOutputDTO coleccionOutputDTO(Coleccion coleccion) {
        return ColeccionOutputDTO.builder()
                .titulo(coleccion.getTitulo())
                .descripcion(coleccion.getDescripcion())
                .handle(coleccion.getHandle())
                .hechos(this.hechoOutputDTO(new HashSet<>(coleccion.filtrarHechos(hechosRepository.findAll()))))
                .build();
    }

    private List<HechoOutputDTO> hechoOutputDTO(Set<IHecho> hechos) {
        return hechos.stream()
                .map(MapperHechos::convertirHechoOutputDTO)
                .collect(Collectors.toList());
    }
}

