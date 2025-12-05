package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.*;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionEditOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionPreviewOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.IAlgoritmoConsenso;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.TipoAlgoritmoConsenso;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.Criterio;
import ar.edu.utn.frba.dds.domain.entities.Etiqueta;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.HechoFilter;
import ar.edu.utn.frba.dds.domain.repository.IColeccionesRepository;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class ColeccionesService implements IColeccionesService {
    private final IColeccionesRepository coleccionesRepository;
    private final IHechosService hechosService;
    private final CriterioFactory criterioFactory;
    private final IFuentesRepository fuentesRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(ColeccionesService.class);

    @Value("${app.pagination.colecciones.size}")
    private int pageSize;

    public ColeccionesService(IColeccionesRepository coleccionesRepository,
                              IHechosService hechosService,
                              CriterioFactory criterioFactory,
                              IFuentesRepository fuentesRepository) {
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

    public List<ColeccionPreviewOutputDTO> findAllPreview(Integer page, TipoAlgoritmoConsenso consenso, Boolean filtrarSinConsenso) {

        List<Coleccion> colecciones;

        if (page == null) {
            colecciones = coleccionesRepository.findAll();
        } else {
            Pageable pageable = PageRequest.of(page, pageSize);
            colecciones = coleccionesRepository.findAll(pageable).getContent();
        }

        // Filtrar por "sin consenso"
        if (filtrarSinConsenso != null && filtrarSinConsenso) {
            colecciones = colecciones.stream()
                    .filter(c -> c.getAlgoritmoConsenso() == null)
                    .toList();
        }
        // Filtrar por tipo de consenso específico
        else if (consenso != null) {
            colecciones = colecciones.stream()
                    .filter(c -> c.getAlgoritmoConsenso() != null &&
                            c.getAlgoritmoConsenso().getTipo() == consenso)
                    .toList();
        }
        // Si no hay filtro, devolver todos

        return colecciones.stream()
                .map(DTOConverter::coleccionPreviewOutputDTO)
                .toList();
    }

    public ColeccionPreviewOutputDTO findByHandlePreview (String handle) {
        return DTOConverter.coleccionPreviewOutputDTO(coleccionesRepository.findByHandle(handle));
    }


    @Override
    public List<HechoOutputDTO> mostrarHechosColeccion(String handle, Boolean curado, HechosFilterDTO filterDTO) {
        // Busco la coleccion
        Coleccion coleccion = coleccionesRepository.findByHandle(handle);
        if (coleccion == null) throw new IllegalArgumentException("No existe la colección");

        // Convertir el DTO en objeto de dominio
        HechoFilter hechosFilter = DTOConverter.convertirHechoFilterInputDTO(filterDTO);

        Integer page = filterDTO.getPage();
        if (page == null) {page = 0;}
        Integer pageSizeLocal = pageSize; // tu pageSize definido en el service

        // Traer hechos usando Criteria API desde Coleccion
        List<Hecho> hechos = findHechosColeccionConFiltros(
                handle,
                curado,
                hechosFilter,
                page,
                pageSizeLocal
        );

        // Convertir a DTO
        return hechos.stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .toList();

    }

    private List<Hecho> findHechosColeccionConFiltros(
            String handle,
            Boolean curado,
            HechoFilter hechosFilter,
            Integer page,
            Integer pageSize
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hecho> query = cb.createQuery(Hecho.class);
        Root<Coleccion> coleccionRoot = query.from(Coleccion.class);

        Join<Coleccion, Hecho> hechosJoin = curado
                ? coleccionRoot.join("listaHechosCurados", JoinType.INNER)
                : coleccionRoot.join("listaHechos", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(coleccionRoot.get("handle"), handle));
        predicates.add(cb.equal(hechosJoin.get("fueEliminado"), false));
        // aplicar filtros del Hecho
        if (hechosFilter.getCategoria() != null) {
            predicates.add(cb.equal(hechosJoin.get("categoria").get("nombre"), hechosFilter.getCategoria()));
        }
        if (hechosFilter.getFReporteDesde() != null) {
            predicates.add(cb.greaterThanOrEqualTo(hechosJoin.get("fechaDeCarga"), hechosFilter.getFReporteDesde()));
        }
        if (hechosFilter.getFReporteHasta() != null) {
            predicates.add(cb.lessThanOrEqualTo(hechosJoin.get("fechaDeCarga"), hechosFilter.getFReporteHasta()));
        }
        if (hechosFilter.getFAconDesde() != null) {
            predicates.add(cb.greaterThanOrEqualTo(hechosJoin.get("fechaDeOcurrencia"), hechosFilter.getFAconDesde()));
        }
        if (hechosFilter.getFAconHasta() != null) {
            predicates.add(cb.lessThanOrEqualTo(hechosJoin.get("fechaDeOcurrencia"), hechosFilter.getFAconHasta()));
        }
        if (hechosFilter.getFuenteId() != null) {
            predicates.add(cb.equal(hechosJoin.get("fuente").get("id"), hechosFilter.getFuenteId()));
        }
        if (hechosFilter.getProvincia() != null) {
            predicates.add(cb.equal(hechosJoin.get("ubicacion").get("provincia"), hechosFilter.getProvincia()));
        }
        if (hechosFilter.getEtiqueta() != null && !hechosFilter.getEtiqueta().isBlank()) {
            Join<Hecho, Etiqueta> joinEtiquetas = hechosJoin.join("etiquetas", JoinType.INNER);
            predicates.add(cb.equal(cb.lower(joinEtiquetas.get("nombre")), hechosFilter.getEtiqueta().toLowerCase()));
        }




        query.select(hechosJoin)
                .where(predicates.toArray(new Predicate[0]))
                .distinct(true);

        TypedQuery<Hecho> typedQuery = entityManager.createQuery(query);

        if (page != null && pageSize != null) {
            typedQuery.setFirstResult(page * pageSize);
            typedQuery.setMaxResults(pageSize);
        }

        return typedQuery.getResultList();
    }


    //-------------------------- OPERACIONES CRUD --------------------------
    @Override
    @Transactional
    public ColeccionOutputDTO crearColeccion(ColeccionInputDTO coleccionInputDTO) {
        var coleccion = new Coleccion(
                this.generarHandleUnico(coleccionInputDTO.getTitulo()),
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
    @Transactional
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
    @Transactional
    public List<Fuente> modificarFuenteColeccion(String handle, List<FuenteInputDTO> fuenteInputDTO){
        Coleccion coleccion = coleccionesRepository.findByHandle(handle);
        List<Fuente> nuevasFuentes = new ArrayList<>();
        fuenteInputDTO.forEach(f -> nuevasFuentes.add( fuentesRepository.findById(f.getId()).orElse(null) ) );
        coleccion.setListaFuentes(nuevasFuentes);
        coleccionesRepository.save(coleccion);

        return coleccion.getListaFuentes();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> modificarColeccion(ColeccionInputDTO coleccionInputDTO) {
        // 1) Buscar la colección por handle
        Coleccion coleccion = coleccionesRepository.findByHandle(coleccionInputDTO.getHandle());

        if (coleccion == null) {
            return ResponseEntity.notFound().build();
        }

        // 2) Actualizar campos simples (título y descripción)
        if (coleccionInputDTO.getTitulo() != null) {
            coleccion.setTitulo(coleccionInputDTO.getTitulo());
        }
        if (coleccionInputDTO.getDescripcion() != null) {
            coleccion.setDescripcion(coleccionInputDTO.getDescripcion());
        }

        // 3) Actualizar criterios si vienen en el DTO
        if (coleccionInputDTO.getListaCriterios() != null && !coleccionInputDTO.getListaCriterios().isEmpty()) {
            List<CriterioInputDTO> criteriosLista = new ArrayList<>(coleccionInputDTO.getListaCriterios());
            List<Criterio> nuevosCriterios = criterioFactory.crearVarios(criteriosLista);

            coleccion.getListaCriterios().clear();
            nuevosCriterios.forEach(coleccion::agregarCriterio);
        }

        // 4) Actualizar algoritmo de consenso si viene en el DTO
        if (coleccionInputDTO.getAlgoritmoConsenso() != null) {
            coleccion.setIAlgoritmoConsenso(
                    DTOConverter.algoritmoConsensoFromDTO(coleccionInputDTO.getAlgoritmoConsenso())
            );
        }

        // 5) Actualizar fuentes si vienen en el DTO
        if (coleccionInputDTO.getListaIdsFuentes() != null) {
            if (coleccionInputDTO.getListaIdsFuentes().isEmpty()) {
                // Si viene una lista vacía, eliminar todas las fuentes
                coleccion.setListaFuentes(new ArrayList<>());
            } else {
                // Buscar las fuentes por ID
                List<Fuente> nuevasFuentes = new ArrayList<>();
                for (Long idFuente : coleccionInputDTO.getListaIdsFuentes()) {
                    fuentesRepository.findById(idFuente).ifPresent(nuevasFuentes::add);
                }
                // El setter se encargará de agregar/eliminar según corresponda
                coleccion.setListaFuentes(nuevasFuentes);
            }
        }

        // 6) Guardar la colección con todos los cambios
        coleccionesRepository.save(coleccion);

        return ResponseEntity.ok().build();
    }

    @Transactional
    @Override
    public ResponseEntity<Void> setDestacadaColeccion(String handle, boolean estaDestacado){
        Coleccion coleccion = coleccionesRepository.findByHandle(handle);
        if (coleccion == null) {
            return ResponseEntity.notFound().build();
        }
        coleccion.setDestacada(estaDestacado);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<ColeccionPreviewOutputDTO> getColeccionesDestacadas() {
        return this.coleccionesRepository.findAllByDestacada(true)
                .stream()
                .map(DTOConverter::coleccionPreviewOutputDTO)
                .toList();
    }

    @Override
    public ColeccionEditOutputDTO findByHandleEditable(String handle) {
        Coleccion coleccion = coleccionesRepository.findByHandle(handle);
        return DTOConverter.coleccionEditOutputDTO(coleccion);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> eliminarColeccion(String handle){
        if(handle == null){
            return ResponseEntity.notFound().build();
        }

        Coleccion coleccion =  coleccionesRepository.findByHandle(handle);
        if (coleccion == null) {
            return ResponseEntity.notFound().build();
        }

        coleccionesRepository.delete(coleccion);
        return ResponseEntity.ok().build();
    }

    //-------------------------------------------------------------------------------
    @Async
    @Override
    @Transactional
    public void actualizarColeccionAsync() {
        this.actualizarColeccionesScheduler();
    }

    @Async
    @Override
    @Transactional
    public void curarColeccionAsync() {
        this.curarColeccionesScheduler();
    }


    @Transactional
    public void actualizarColeccionesScheduler(){
        logger.info("Actualizando Colecciones Scheduler");

        //busco colecciones actualzables
        List <Coleccion> coleccionesActualizables = coleccionesRepository.findAll().stream().filter(Coleccion::getActualizarHechos).toList();

        //obtengo las fuentes de esas colecciones
        Set<Fuente> fuentes = coleccionesActualizables.stream()
                .flatMap(coleccion -> coleccion.getListaFuentes().stream())
                .collect(Collectors.toSet()); // set para no repetir

        // hago un map de fuentes y lista de hechos de las fuentes
        Map<Fuente, List<Hecho>> listaHechosXFuente = new HashMap<>();
        for (Fuente fuente : fuentes) {
            List<Hecho> hechosFuente = this.hechosService.findByFuente(fuente);
            listaHechosXFuente.put(fuente, hechosFuente);
        }

        // por cada coleccion, le paso sus hechos correspondientes y le digo que se actualize
        for (Coleccion coleccion : coleccionesActualizables) {
            logger.info("Coleccion a actualizar; Titulo: {}", coleccion.getTitulo());

            List<Hecho> hechosParaColeccion = coleccion.getListaFuentes().stream()
                    .flatMap(fuente -> listaHechosXFuente.getOrDefault(fuente, List.of()).stream())
                    .toList();

            coleccion.actualizarHechos(hechosParaColeccion);
        }
    }

    @Transactional
    public void curarColeccionesScheduler(){
        logger.info("Curando Colecciones Scheduler");

        //busco colecciones actualzables
        List <Coleccion> coleccionesCurables = coleccionesRepository.findAll().stream().filter(Coleccion::getCurarHechos).toList();

        //obtengo las fuentes de esas colecciones
        Set<Fuente> fuentes = coleccionesCurables.stream()
                .flatMap(coleccion -> coleccion.getListaFuentes().stream())
                .collect(Collectors.toSet()); // set para no repetir

        // hago un map de fuentes y lista de hechos de las fuentes
        Map<Fuente, List<Hecho>> listaHechosXFuente = new HashMap<>();
        for (Fuente fuente : fuentes) {
            List<Hecho> hechosFuente = this.hechosService.findByFuente(fuente);
            listaHechosXFuente.put(fuente, hechosFuente);
        }

        // por cada coleccion, le paso sus hechos correspondientes y le digo que se cure
        for (Coleccion coleccion : coleccionesCurables) {
            logger.info("Coleccion a actualizar; Titulo: {}", coleccion.getTitulo());

            List< List<Hecho> > hechosFuentesColeccion = listaHechosXFuente.entrySet().stream()
                    .filter(entry -> coleccion.getListaFuentes().contains(entry.getKey()))
                    .map(entry -> (List<Hecho>) new ArrayList<>(entry.getValue()))
                    .toList();

            coleccion.curarHechos( hechosFuentesColeccion );
        }
    }

    @Override
    @Transactional
    public void notificarActualizacionFuentes(List<Fuente> fuentes){
        List<Coleccion> colecciones = coleccionesRepository.findAll();
        colecciones = colecciones.stream()
                .filter( c -> c.getListaFuentes().stream().anyMatch( fuentes::contains) )
                .toList();
        colecciones.forEach(c -> c.setActualizarHechos(true));
    }

    @Override
    @Transactional
    public void notificarFuenteEliminada(Fuente fuente){
        List<Fuente> fuentes = new ArrayList<>();
        fuentes.add(fuente);

        List<Coleccion> colecciones = coleccionesRepository.findAllByListaFuentesContaining(fuentes);
        for (Coleccion c : colecciones) { c.eliminarFuente(fuente); }
        coleccionesRepository.saveAll(colecciones);
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



    // --- METODOS PRIVADOS --- //

    private String generarHandleUnico(String titulo) {
        // Normalizar título: quitar espacios, acentos, etc.
        String baseHandle = titulo.toLowerCase().replaceAll("[^a-z0-9]", "-");

        String handle = baseHandle;
        int contador = 1;

        while (coleccionesRepository.existsColeccionByHandle(handle)) {
            handle = baseHandle + "-" + contador++;
        }

        return handle;
    }




    // --- TESTEO --- //
    @Transactional
    public ColeccionOutputDTO actualizarColeccionManual(String handle) {
        // 1. Obtener la colección
        Coleccion coleccion = this.coleccionesRepository.findByHandle(handle);
        if (coleccion == null) {
            throw new IllegalArgumentException("No existe la colección con handle: " + handle);
        }

        // 2. Obtener fuentes únicas de la colección
        List<Fuente> fuentes = coleccion.getListaFuentes().stream()
                .distinct() // eliminar duplicados
                .toList();

        // 3. Mapear cada fuente a su lista de hechos
        Map<Fuente, List<Hecho>> listaHechosXFuente = new HashMap<>();
        for (Fuente fuente : fuentes) {
            List<Hecho> hechos = this.hechosService.findByFuente(fuente);
            listaHechosXFuente.put(fuente, hechos);
        }

        // 4. Llamar a actualizarHechos pasando la lista combinada de hechos
        List<Hecho> todosLosHechos = listaHechosXFuente.values().stream()
                .flatMap(List::stream)
                .toList();

        coleccion.actualizarHechos(todosLosHechos);

        // 5. Devolver DTO
        return DTOConverter.coleccionOutputDTO(coleccion);
    }

    @Transactional
    public ColeccionOutputDTO curarColeccionManual(String handle) {
        Coleccion coleccion = coleccionesRepository.findByHandle(handle);
        if (coleccion == null) throw new IllegalArgumentException("No existe la colección");

        // Obtener todas las fuentes de la colección
        List<Fuente> fuentes = coleccion.getListaFuentes().stream()
                .distinct()
                .toList();

        // Obtener hechos por fuente
        Map<Fuente, List<Hecho>> hechosPorFuente = new HashMap<>();
        for (Fuente fuente : fuentes) {
            List<Hecho> hechos = this.hechosService.findByFuente(fuente);
            hechosPorFuente.put(fuente, hechos);
        }

        // Aplanar todos los hechos en una lista total
        List<List<Hecho>> listaHechosFuentes = new ArrayList<>(hechosPorFuente.values());

        // Pasar los hechos y el map de hechos por fuente al métod0 de curación
        coleccion.curarHechos(listaHechosFuentes);

        return DTOConverter.coleccionOutputDTO(coleccion);
    }

}

