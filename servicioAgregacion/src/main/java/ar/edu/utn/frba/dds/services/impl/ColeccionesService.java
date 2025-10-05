package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.*;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.Criterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.HechoFilter;
import ar.edu.utn.frba.dds.domain.repository.IColeccionesRepository;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class ColeccionesService implements IColeccionesService {
    private final IColeccionesRepository coleccionesRepository;
    private final IHechosService hechosService;
    private final CriterioFactory criterioFactory;
    private final IFuentesRepository fuentesRepository;
    private final ICategoriaService categoriaService;

    private static final Logger logger = LoggerFactory.getLogger(ColeccionesService.class);

    public ColeccionesService(IColeccionesRepository coleccionesRepository,
                              IHechosService hechosService,
                              CriterioFactory criterioFactory,
                              IFuentesRepository fuentesRepository,
                              ICategoriaService categoriaService) {
        this.coleccionesRepository = coleccionesRepository;
        this.hechosService = hechosService;
        this.criterioFactory  = criterioFactory;
        this.fuentesRepository = fuentesRepository;
        this.categoriaService = categoriaService;
    }

    @Override
    public ColeccionOutputDTO findByHandle(String handle) {
        return DTOConverter.coleccionOutputDTO(coleccionesRepository.findByHandle(handle));
    }

    @Override
    public List<ColeccionOutputDTO> findAll() {
        return coleccionesRepository.findAll().stream()
                .map(DTOConverter::coleccionOutputDTO)
                .collect(Collectors.toList());
    }

    //-------------------------- OPERACIONES CRUD --------------------------
    @Override
    public ColeccionOutputDTO crearColeccion(ColeccionInputDTO coleccionInputDTO) {
        var coleccion = new Coleccion(
                null,
                coleccionInputDTO.getTitulo(),
                coleccionInputDTO.getDescripcion(),
                DTOConverter.algoritmoConsensoFromDTO(coleccionInputDTO.getAlgoritmoConsenso() ));
        // extra
        if ( coleccionInputDTO.getListaIdsFuentes() != null) {
            coleccionInputDTO.getListaIdsFuentes().
                    forEach(fuente -> coleccion.agregarFuente( fuentesRepository.findById(fuente).orElse(null)  ) );
        }
        if (coleccionInputDTO.getListaCriterios() != null) {
            coleccionInputDTO.getListaCriterios().forEach(n->coleccion.agregarCriterio(criterioFactory.crear(n)));
        }
        coleccionesRepository.save(coleccion);
        return DTOConverter.coleccionOutputDTO(coleccion);
    }

    @Override
    public ColeccionOutputDTO modificarColeccionBasica(ColeccionInputDTO coleccionInputDTO) {
        // 1) Cargo la colección existente por handle
        Coleccion coleccion = coleccionesRepository.findByHandle(coleccionInputDTO.getHandle());

        // 2) Actualizo campos simples
        coleccion.setTitulo(coleccionInputDTO.getTitulo());
        coleccion.setDescripcion(coleccionInputDTO.getDescripcion());
        coleccionesRepository.save(coleccion);

        return DTOConverter.coleccionOutputDTO(coleccion);
    }

    @Override
    public ResponseEntity<Void> modificarCriteriosColeccion (String handle, List<CriterioInputDTO> listaCriterioInputDTO){
        Coleccion coleccion = coleccionesRepository.findByHandle(handle);

        if (coleccion == null){
            return ResponseEntity.notFound().build();
        }

        Set<Criterio> nuevos = new HashSet<>(criterioFactory.crearVarios(listaCriterioInputDTO));
        coleccion.setListaCriterios(nuevos);
        coleccionesRepository.save(coleccion);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> modificarConsensoColeccion (String handle, AlgoritmoConsensoDTO consensoDTO) {
        Coleccion coleccion = coleccionesRepository.findByHandle(handle);
        if (coleccion == null){
            return ResponseEntity.notFound().build();
        }

        coleccion.setIAlgoritmoConsenso( DTOConverter.algoritmoConsensoFromDTO(consensoDTO) );
        coleccionesRepository.save(coleccion);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<Fuente> modificarFuenteColeccion(String handle, List<FuenteInputDTO> fuenteInputDTO){
        Coleccion coleccion = coleccionesRepository.findByHandle(handle);
        List<Fuente> nuevasFuentes = new ArrayList<>();
        fuenteInputDTO.forEach(f -> nuevasFuentes.add( fuentesRepository.findById(f.getId()).orElse(null) ) );
        coleccion.setListaFuentes(nuevasFuentes);
        coleccionesRepository.save(coleccion);

        return coleccion.getListaFuentes();
    }

    @Override
    public ResponseEntity<Void> eliminarColeccion(ColeccionInputDTO coleccionInputDTO){
        if(coleccionInputDTO == null){
            return ResponseEntity.notFound().build();
        }

        if (coleccionesRepository.findByHandle(coleccionInputDTO.getHandle()) == null) {
            return ResponseEntity.notFound().build();
        }

        Coleccion coleccion = DTOConverter.coleccionFromInputDTO(coleccionInputDTO);
        coleccionesRepository.delete(coleccion);
        return ResponseEntity.ok().build();
    }

    //-------------------------------------------------------------------------------

    public void actualizarColeccionesScheduler(){
        logger.info("Actualizando Colecciones Scheduler");
/*aca esta*/List <Coleccion> coleccionesActualizables = coleccionesRepository.findAll().stream().filter(Coleccion::getActualizarHechos).toList();
/*el error */coleccionesActualizables.forEach(n->logger.info("Coleccion a actualizar; Titulo: {}", n.getTitulo()));
        coleccionesActualizables.forEach(Coleccion::actualizarHechos);
    }

    public void curarColeccionesScheduler(){
        logger.info("Curando Colecciones Scheduler");
        List <Coleccion> coleccionesActualizables = coleccionesRepository.findAll().stream().filter(Coleccion::getCurarHechos).toList();
        coleccionesActualizables.forEach(n->logger.info("Coleccion a curar; Titulo: {}", n.getTitulo()));
        coleccionesActualizables.forEach(Coleccion::curarHechos);
    }

    @Override
    public void notificarActualizacionFuentes(List<Fuente> fuentes){
        List<Coleccion> colecciones = coleccionesRepository.findAll();
        colecciones = colecciones.stream()
                .filter( c -> c.getListaFuentes().stream().anyMatch( fuentes::contains) )
                .toList();
        colecciones.forEach(c -> c.setActualizarHechos(true));
    }

    @Override
    public void notificarFuenteEliminada(Fuente fuente){
        List<Coleccion> colecciones =  coleccionesRepository.findAll().stream()
                .filter(c -> c.getListaFuentes().contains(fuente)).toList();
        colecciones.forEach(c -> c.eliminarFuente(fuente));
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

    public List<Hecho> getHechosColeccionFiltrados(String handle, List<Criterio> criterios, Boolean visualizarCurado){
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


    /*public HechosPaginadosResponseDTO paginarHechos(List<Hecho> hechos, int page, int size){
        if (page < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parámetros de paginación inválidos");
        }

        if (hechos == null || hechos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada o sin hechos");
        }

        int fromIndex = Math.min(page * size, hechos.size());
        int toIndex = Math.min(fromIndex + size, hechos.size());
        List<HechoOutputDTO> hechosPaginados = hechos.subList(fromIndex, toIndex).stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .toList();

        return new HechosPaginadosResponseDTO(hechosPaginados, page, size, hechos.size());

    }*/

    @Override
    public List<HechoOutputDTO> mostrarHechosColeccion(String handle, Boolean curado, HechosFilterDTO filterDTO) {
        // Convertir el DTO en objeto de dominio
        HechoFilter filter = DTOConverter.convertirHechoFilterInputDTO(filterDTO);

        // Buscar la categoría si corresponde
        Categoria categoria = null;
        if (filter.getCategoria() != null) {
            categoria = this.categoriaService.findByNombre(filter.getCategoria());
        }

        // Generar criterios con la factory
        List<Criterio> criterios = this.criterioFactory.crearCriteriosParametros(categoria, filter);

        // btener hechos según haya o no criterios
        List<Hecho> hechos;
        if (criterios.isEmpty()) {
            hechos = this.getHechosColeccion(handle, curado);
        } else {
            hechos = this.getHechosColeccionFiltrados(handle, criterios, curado);
        }

        return DTOConverter.hechoOutputDTO(hechos);
    }
}

