package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.ColeccionesRepository;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ColeccionesService implements IColeccionesService {
    private final ColeccionesRepository coleccionesRepository;
    private final IHechosService hechosService;
    private final CriterioFactory criterioFactory;
    private final IFuentesRepository fuentesRepository;

    public ColeccionesService(ColeccionesRepository coleccionesRepository, IHechosService hechosService) {
        this.coleccionesRepository = coleccionesRepository;
        this.hechosService = hechosService;
        this.criterioFactory  = criterioFactory;
        this.fuentesRepository = fuentesRepository;
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
    public void crearColeccion(ColeccionInputDTO coleccionInputDTO) {
        var coleccion = new Coleccion(
                coleccionInputDTO.getHandle(),
                coleccionInputDTO.getTitulo(),
                coleccionInputDTO.getDescripcion());
        //TODO al crear no viene con handle, modificar DTO y hacer que se cree el handle en el momento de la creacion
        coleccionInputDTO.getListaCriterios().forEach(coleccion::agregarCriterio);
    }

    @Override
    public void modificarColeccion(ColeccionInputDTO coleccionInputDTO) {
        // 1) Cargo la colección existente por handle
        Coleccion coleccion = coleccionesRepository.findByHandle(coleccionInputDTO.getHandle());

        // 2) Actualizo campos simples
        coleccion.setTitulo(coleccionInputDTO.getTitulo());
        coleccion.setDescripcion(coleccionInputDTO.getDescripcion());

        // 3) Sincronizo FUENTES
        List<IFuente> nuevasFuentes = fuentesRepository.findAllById(coleccionInputDTO.getListaIdsFuentes());
        // elimino las que ya no están
        coleccion.getListaFuentes().stream()
                .filter(f -> !nuevasFuentes.contains(f))
                .forEach(coleccion::eliminarFuente);
        // agrego las que faltan
        nuevasFuentes.stream()
                .filter(f -> !coleccion.getListaFuentes().contains(f))
                .forEach(coleccion::agregarFuente);

        // 4) Sincronizo CRITERIOS
        // primero elimino todos los existentes
        new HashSet<>(coleccion.getListaCriterios())
                .forEach(coleccion::eliminarCriterio);
        // luego creo e inserto los nuevos desde el DTO
        coleccionInputDTO.getListaCriterios().stream()
                .map(criterioFactory::crear)
                .forEach(coleccion::agregarCriterio);

        // 5) Recalculo y curo hechos
        coleccion.actualizarHechos();
        coleccion.curarHechos();

        // 6) Persiste los cambios
        coleccionesRepository.save(coleccion);
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
        coleccionesActualizables.stream()
                .map(this::toInputDTO)
                .forEach(this::actualizarColeccion);
    }

    public void actualizarColeccion(ColeccionInputDTO coleccionInputDTO){
        Coleccion coleccion = this.coleccionFromInputDTO(coleccionInputDTO);
        List <Hecho> hechos = hechosService.findByFuente(coleccion.getListaFuentes());
        coleccion.actualizarHechos(hechos);
    }

    // actualizar coleccion -> volver a calcular los hechos que le pertenece (CARO Y LENTO) -> hacerlo lo minimo posible
    // cuando actualizar -> actualizamos los hechos de la fuente o actualizamos los criterios

    public List<HechoOutputDTO> mostrarHechosColeccion(String handle){
        Coleccion coleccion = this.coleccionesRepository.findByHandle(handle); //considera hechos estaticos y dinamicos
        List<Hecho> hechosAMostar = coleccion.getListaHechos();
        if (coleccion.getListaFuentes().stream().anyMatch(f -> f.getTipo().equals(TipoFuente.PROXY))){
            List<Hecho> hechosProxy = hechosService.obtenerHechosProxy();
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

    private Coleccion coleccionFromInputDTO(ColeccionInputDTO input) {
        return Coleccion.builder()
                .titulo(input.getTitulo())
                .descripcion(input.getDescripcion())
                .handle(input.getHandle())
                .build();
    }

    private ColeccionInputDTO toInputDTO(Coleccion coleccion) {
        return ColeccionInputDTO.builder()
                .titulo(coleccion.getTitulo())
                .descripcion(coleccion.getDescripcion())
                .handle(coleccion.getHandle())
                .build();
    }
}

