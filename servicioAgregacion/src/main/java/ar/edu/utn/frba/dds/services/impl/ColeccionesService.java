package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechosPaginadosResponseDTO;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.ConsensoAbsoluto;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.ConsensoMayoriaSimple;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.IAlgoritmoConsenso;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.ColeccionesRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.FuentesRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.HechosRepository;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ColeccionesService implements IColeccionesService {
    private final ColeccionesRepository coleccionesRepository;
    private final IHechosService hechosService;
    private final CriterioFactory criterioFactory;
    private final IFuentesRepository fuentesRepository;
    private final HechosRepository hechosRepository;


    public ColeccionesService(ColeccionesRepository coleccionesRepository,
                              IHechosService hechosService,
                              CriterioFactory criterioFactory,
                              FuentesRepository fuentesRepository,
                              HechosRepository hechosRepository) {
        this.coleccionesRepository = coleccionesRepository;
        this.hechosService = hechosService;
        this.criterioFactory  = criterioFactory;
        this.fuentesRepository = fuentesRepository;
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

    //-------------------------- OPERACIONES CRUD --------------------------

    @Override
    public void crearColeccion(ColeccionInputDTO coleccionInputDTO) {
        var coleccion = new Coleccion(
                coleccionInputDTO.getHandle(),
                coleccionInputDTO.getTitulo(),
                coleccionInputDTO.getDescripcion());
        //TODO al crear no viene con handle, modificar DTO y hacer que se cree el handle en el momento de la creacion
        coleccionInputDTO.getListaCriterios().forEach(n->coleccion.agregarCriterio(criterioFactory.crear(n)));
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

        // 6) Modifico el algoritmo de consenso
        AlgoritmoConsensoDTO algDTO = coleccionInputDTO.getAlgoritmoConsenso();
        if (algDTO != null) {
            String tipo = algDTO.getTipo();
            Map<String,String> p = algDTO.getParametros();

            IAlgoritmoConsenso nuevoAlg;
            switch (tipo) {
                case "mayoriaSimple":
                    nuevoAlg = new ConsensoMayoriaSimple();
                    break;

                default:
                    throw new IllegalArgumentException("Algoritmo desconocido: " + tipo);
            }

            coleccion.setAlgoritmoConsenso(nuevoAlg);
            coleccion.setCurarHechos(false);

        // 7) Persiste los cambios
        coleccionesRepository.save(coleccion);
    }

    public void eliminarColeccion(ColeccionInputDTO coleccionInputDTO){
        Coleccion coleccion = this.coleccionFromInputDTO(coleccionInputDTO);
        coleccionesRepository.delete(coleccion);
    }

    public void eliminarColeccionByHandle(String handle){
        Coleccion coleccion = coleccionesRepository.findByHandle(handle);
        coleccionesRepository.delete(coleccion);
    }

    //-------------------------------------------------------------------------------

    public void actualizarColeccionesScheduler(){
        List <Coleccion> coleccionesActualizables = coleccionesRepository.findAll().stream().filter(Coleccion::getActualizarHechos).toList();
        coleccionesActualizables.forEach(Coleccion::actualizarHechos);
    }

    public void notificarActualizacionFuentes(List<IFuente> fuentes){
        List<Coleccion> colecciones = coleccionesRepository.findAll();
        colecciones = colecciones.stream()
                .filter( c -> c.getListaFuentes().stream().anyMatch( fuentes::contains) )
                .toList();
        colecciones.forEach(c -> c.setActualizarHechos(true));
    }


    // actualizar coleccion -> volver a calcular los hechos que le pertenece (CARO Y LENTO) -> hacerlo lo minimo posible
    // cuando actualizar -> actualizamos los hechos de la fuente o actualizamos los criterios

    public List<Hecho> getHechosColeccion(String handle, Boolean visualizarCurado){
        Coleccion coleccion = this.coleccionesRepository.findByHandle(handle); //considera hechos estaticos y dinamicos
        if (visualizarCurado) {
            return coleccion.getHechosCurados();
        }
        return coleccion.getHechos();
    }

    public List<Hecho> getHechosColeccionFiltrados(String handle,List<ICriterio> criterios, Boolean visualizarCurado){
        Coleccion coleccion = this.coleccionesRepository.findByHandle(handle);
        if (visualizarCurado) {
            return coleccion.getHechosCuradosYFiltrados(criterios);
        }
        return coleccion.getHechosConFiltro(criterios);
    }

    public List<Hecho> mostrarHechosColecciones(String handle){
        Coleccion coleccion = this.coleccionesRepository.findByHandle(handle); //considera hechos estaticos y dinamicos
        return coleccion.getHechos();
    }


    public HechosPaginadosResponseDTO paginarHechos(List<Hecho> hechos, int page, int size){
        if (page < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parámetros de paginación inválidos");
        }

        if (hechos == null || hechos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada o sin hechos");
        }

        int fromIndex = Math.min(page * size, hechos.size());
        int toIndex = Math.min(fromIndex + size, hechos.size());
        List<HechoOutputDTO> hechosPaginados = hechos.subList(fromIndex, toIndex);

        return new HechosPaginadosResponseDTO(hechosPaginados, page, size, hechos.size());

    }

    @Override
    public HechosPaginadosResponseDTO mostrarHechosColeccion(String handle, int page, int size,List<CriterioInputDTO> criterios, Boolean curado){
        List<ICriterio> criteriosEntidades = this.criterioFactory.crearVarios(criterios);

        if(criterios == null || criterios.isEmpty()){
            List<Hecho> hechos = this.getHechosColeccion(handle,curado);
            return this.paginarHechos(hechos,page,size);
        }

        List<Hecho> hechosFiltrados = this.getHechosColeccionFiltrados(handle,criteriosEntidades, curado);
        return this.paginarHechos(hechosFiltrados,page,size);

    }



    private ColeccionOutputDTO coleccionOutputDTO(Coleccion coleccion) {
        return ColeccionOutputDTO.builder()
                .titulo(coleccion.getTitulo())
                .descripcion(coleccion.getDescripcion())
                .handle(coleccion.getHandle())
                .build();
    }

    private Coleccion coleccionFromInputDTO(ColeccionInputDTO input) {
        return new Coleccion(
                input.getTitulo(),
                input.getDescripcion(),
                input.getHandle()), null;
    }

    private ColeccionInputDTO toInputDTO(Coleccion coleccion) {
        return ColeccionInputDTO.builder()
                .titulo(coleccion.getTitulo())
                .descripcion(coleccion.getDescripcion())
                .handle(coleccion.getHandle())
                .build();
    }
}

