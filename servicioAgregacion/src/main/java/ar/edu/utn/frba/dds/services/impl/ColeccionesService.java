package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.ColeccionesRepository;
import ar.edu.utn.frba.dds.domain.repository.impl.FuentesRepository;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ColeccionesService implements IColeccionesService {
    private final ColeccionesRepository coleccionesRepository;
    private final IHechosService hechosService;
    private final CriterioFactory criterioFactory;
    private final IFuentesRepository fuentesRepository;

    private static final Logger logger = LoggerFactory.getLogger(ColeccionesService.class);

    public ColeccionesService(ColeccionesRepository coleccionesRepository,
                              IHechosService hechosService,
                              CriterioFactory criterioFactory,
                              FuentesRepository fuentesRepository) {
        this.coleccionesRepository = coleccionesRepository;
        this.hechosService = hechosService;
        this.criterioFactory  = criterioFactory;
        this.fuentesRepository = fuentesRepository;
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
            coleccionInputDTO.getListaIdsFuentes().forEach(fuente -> coleccion.agregarFuente(fuentesRepository.findById( fuente ))); }
        if (coleccionInputDTO.getListaCriterios() != null) {
            coleccionInputDTO.getListaCriterios().forEach(n->coleccion.agregarCriterio(criterioFactory.crear(n)));
        }
        coleccionesRepository.save(coleccion);
        return DTOConverter.coleccionOutputDTO(coleccion);
    }

    @Override
    public void modificarColeccionBasica(ColeccionInputDTO coleccionInputDTO) {
        // 1) Cargo la colección existente por handle
        Coleccion coleccion = coleccionesRepository.findByHandle(coleccionInputDTO.getHandle());

        // 2) Actualizo campos simples
        coleccion.setTitulo(coleccionInputDTO.getTitulo());
        coleccion.setDescripcion(coleccionInputDTO.getDescripcion());
        coleccionesRepository.save(coleccion);
    }

    @Override
    public void modificarCriteriosColeccion (String handle, List<CriterioInputDTO> listaCriterioInputDTO){
        Coleccion coleccion = coleccionesRepository.findByHandle(handle);
        Set<ICriterio> nuevos = new HashSet<>(criterioFactory.crearVarios(listaCriterioInputDTO));
        coleccion.setListaCriterios(nuevos);
        coleccionesRepository.save(coleccion);
    }

    @Override
    public void modificarConsensoColeccion (String handle, AlgoritmoConsensoDTO consensoDTO) {
        Coleccion coleccion = coleccionesRepository.findByHandle(handle);
        coleccion.setAlgoritmoConsenso( DTOConverter.algoritmoConsensoFromDTO(consensoDTO) );
        coleccionesRepository.save(coleccion);
    }

    @Override
    public void modificarFuenteColeccion(String handle, List<FuenteInputDTO> fuenteInputDTO){
        Coleccion coleccion = coleccionesRepository.findByHandle(handle);
        List<IFuente> nuevasFuentes = new ArrayList<>();
        fuenteInputDTO.forEach(f -> nuevasFuentes.add( fuentesRepository.findById( f.getId()) ) );
        coleccion.setListaFuentes(nuevasFuentes);
        coleccionesRepository.save(coleccion);
    }

    @Override
    public void eliminarColeccion(ColeccionInputDTO coleccionInputDTO){
        if(coleccionInputDTO == null){
            throw new IllegalArgumentException("argumento nulo");
        }
        if (coleccionesRepository.findByHandle(coleccionInputDTO.getHandle()) != null) {
            Coleccion coleccion = DTOConverter.coleccionFromInputDTO(coleccionInputDTO);
            coleccionesRepository.delete(coleccion);
        } else {
            throw new RuntimeException("No se encontro la coleccion");
        }
    }

    //-------------------------------------------------------------------------------

    public void actualizarColeccionesScheduler(){
        logger.info("Actualizando Colecciones Scheduler");
        List <Coleccion> coleccionesActualizables = coleccionesRepository.findAll().stream().filter(Coleccion::getActualizarHechos).toList();
        coleccionesActualizables.forEach(n->logger.info("Coleccion a actualizar; Titulo: {}", n.getTitulo()));
        coleccionesActualizables.forEach(Coleccion::actualizarHechos);
    }

    @Override
    public void notificarActualizacionFuentes(List<IFuente> fuentes){
        List<Coleccion> colecciones = coleccionesRepository.findAll();
        colecciones = colecciones.stream()
                .filter( c -> c.getListaFuentes().stream().anyMatch( fuentes::contains) )
                .toList();
        colecciones.forEach(c -> c.setActualizarHechos(true));
    }

    @Override
    public void notificarFuenteEliminada(IFuente fuente){
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
    public List<HechoOutputDTO> mostrarHechosColeccion(String handle, Boolean curado, CategoriaInputDTO categoria, LocalDateTime fReporteDesde, LocalDateTime fReporteHasta, LocalDate fAconDesde,LocalDate fAconHasta, UbicacionInputDTO ubicacion){
        List<ICriterio> criteriosEntidades = this.criterioFactory.crearCriteriosParametros(DTOConverter.categoriaInputDTO(categoria),fReporteDesde,fReporteHasta,fAconDesde,fAconHasta,DTOConverter.convertirUbicacion(ubicacion));

        if(criteriosEntidades.isEmpty()){
            List<Hecho> hechos = this.getHechosColeccion(handle,curado);
            return DTOConverter.hechoOutputDTO(hechos);

        }

        List<Hecho> hechosFiltrados = this.getHechosColeccionFiltrados(handle,criteriosEntidades, curado);
        return DTOConverter.hechoOutputDTO(hechosFiltrados);

    }
}

