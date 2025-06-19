package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.repository.impl.ColeccionesRepository;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColeccionesService implements IColeccionesService {
    private final ColeccionesRepository coleccionesRepository;
    private final IHechosService hechosService;

    public ColeccionesService(ColeccionesRepository coleccionesRepository, IHechosService hechosService) {
        this.coleccionesRepository = coleccionesRepository;
        this.hechosService = hechosService;
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
        Coleccion coleccion = new Coleccion(
                coleccionInputDTO.getTitulo(),
                coleccionInputDTO.getDescripcion());

        if (coleccionInputDTO.getHandle() != null) {
            coleccion.setHandle(coleccionInputDTO.getHandle());
        }

        coleccionInputDTO.getListaCriterios().forEach(coleccion::agregarCriterio);

        coleccionesRepository.save(coleccion);
        return this.coleccionOutputDTO(coleccion);
    }

    @Override
    public List<HechoOutputDTO> hechosDeLaColeccion(String handle) {
        return coleccionesRepository.findByHandle(handle).getListaHechos().stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .collect(Collectors.toList());
    }


    public void actualizarColeccionesScheduler(){
        List <Coleccion> coleccionesActualizables = coleccionesRepository.findAll().stream().filter(Coleccion::getActualizarHechos).toList();
        //TODO ver como actualizar el booleano de las colecciones
        coleccionesActualizables.forEach(Coleccion::actualizarHechos);
    }

    // actualizar coleccion -> volver a calcular los hechos que le pertenece (CARO Y LENTO) -> hacerlo lo minimo posible
    // cuando actualizar -> actualizamos los hechos de la fuente o actualizamos los criterios

    public List<HechoOutputDTO> mostrarHechosColeccion(String handle){
        Coleccion coleccion = this.coleccionesRepository.findByHandle(handle); //considera hechos estaticos y dinamicos
        List<IFuente> fuentesAConsumir = coleccion.getListaFuentes().stream()
                .filter(f -> f.getTipo() == TipoFuente.PROXY)
                .toList();
        List<Hecho> hechosConsumidos = new ArrayList<>();
        for (IFuente fuente: fuentesAConsumir){
            //cargamos los hechos de las fuentes a consumir
            hechosConsumidos.addAll(hechosService.consumirFuente(fuente));
        }
        return DTOConverter.hechoOutputDTO(coleccion.getHechosVisualizar(hechosConsumidos))  ;
    }

    private ColeccionOutputDTO coleccionOutputDTO(Coleccion coleccion) {
        return ColeccionOutputDTO.builder()
                .titulo(coleccion.getTitulo())
                .descripcion(coleccion.getDescripcion())
                .handle(coleccion.getHandle())
                .build();
    }
}

