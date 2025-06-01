package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;
import ar.edu.utn.frba.dds.domain.repository.impl.ColeccionesRepository;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.stereotype.Service;

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
        var coleccion = new Coleccion(
                coleccionInputDTO.getHandle(),
                coleccionInputDTO.getTitulo(),
                coleccionInputDTO.getDescripcion());
        //TODO al crear no viene con handle, modificar DTO y hacer que se cree el handle en el momento de la creacion
        coleccionInputDTO.getListaCriterios().forEach(coleccion::agregarCriterio);

        return this.coleccionOutputDTO(coleccion);
    }

    @Override
    public List<HechoOutputDTO> hechosDeLaColeccionByHandle(String handle) {
        return coleccionesRepository.hechosByHandle(handle,hechosService.findAll()).stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .collect(Collectors.toList());
    }

    public void actualizarColeccionesScheduler(){
        List <Coleccion> coleccionesActualizables = coleccionesRepository.findAll().stream().filter(Coleccion::getActualizarHechos).toList();
        //TODO ver como actualizar el booleano de las colecciones
        coleccionesActualizables.forEach(this::actualizarColeccion);
    }

    public void actualizarColeccion(Coleccion coleccion){
        List <HechoBase> hechos = hechosService.findByTipoFuente(coleccion.getListaTipoFuentes());
        coleccion.actualizarHechos(hechos);
    }

    // actualizar coleccion -> volver a calcular los hechos que le pertenece (CARO Y LENTO) -> hacerlo lo minimo posible
    // cuando actualizar -> actualizamos los hechos de la fuente o actualizamos los criterios

    public List<HechoOutputDTO> mostrarHechosColeccion(String handle){
        Coleccion coleccion = this.coleccionesRepository.findByHandle(handle); //considera hechos estaticos y dinamicos
        List<HechoBase> hechosAMostar = coleccion.getListaHechos();
        if (coleccion.getListaTipoFuentes().contains(TipoFuente.PROXY)){
            List<HechoBase> hechosProxy = hechosService.obtenerHechosProxy();
            hechosAMostar.addAll(hechosProxy);
        }
        return DTOConverter.hechoOutputDTO(hechosAMostar);
        //TODO si modificamos para que las colecciones sean a partir de fuentes especificas sera diferente
    }

    private ColeccionOutputDTO coleccionOutputDTO(Coleccion coleccion) {
        return ColeccionOutputDTO.builder()
                .titulo(coleccion.getTitulo())
                .descripcion(coleccion.getDescripcion())
                .handle(coleccion.getHandle())
                .build();
    }
}

