package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.HechosFilterDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoMapaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Etiqueta;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Geolocalizadores.Georef;
import ar.edu.utn.frba.dds.domain.entities.Geolocalizadores.IGeoLocalizador;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.HechoFilter;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.services.IEtiquetasService;
import ar.edu.utn.frba.dds.services.IHechosService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;
    private final ICategoriaService categoriaService;
    private final CriterioFactory criterioFactory;
    private final IEtiquetasService etiquetaService;

    private IGeoLocalizador geolocalizador = new Georef();

    private static final Logger logger = LoggerFactory.getLogger(HechosService.class);

    @Value("${app.pagination.hechos.size}") // 20 es el valor por defecto si no está definido
    private int pageSize;

    public HechosService(IHechosRepository hechosRepository,
                         ICategoriaService categoriaService,
                         CriterioFactory criterioFactory,
                         IEtiquetasService etiquetaService) {
        this.hechosRepository = hechosRepository;
        this.categoriaService = categoriaService;
        this.criterioFactory = criterioFactory;
        this.etiquetaService = etiquetaService;
    }

    @Override
    public List<Hecho> findAll(){ return this.hechosRepository.findAll(); }

    @Override
    public List<HechoOutputDTO> findAllOutput(){
        return this.findAll()
                .stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .toList();
    }

    @Override
    public HechoOutputDTO findByID(Long id) {
        Hecho hecho = this.hechosRepository.getHechoById(id);
        return DTOConverter.convertirHechoOutputDTO(hecho);
    }

    @Override
    public Hecho findEntidadPorId(Long id){
        return this.hechosRepository.getHechoById(id);
    }

    @Override
    public List<HechoOutputDTO> getHechos(HechosFilterDTO filterDTO){
        HechoFilter hechosFilter = DTOConverter.convertirHechoFilterInputDTO(filterDTO);

        if (filterDTO.getPage() == null) { // no tiene pagina -> devuelvo tod0s los hechos
            return this.hechosRepository.findAll(buildHechosSpecification(hechosFilter))
                    .stream()
                    .map(DTOConverter::convertirHechoOutputDTO)
                    .toList();
        }

        Pageable pageable = PageRequest.of(filterDTO.getPage(), pageSize);

        return this.hechosRepository.findAll(buildHechosSpecification(hechosFilter), pageable)
                .stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .toList();
    }


    @Override
    public List<HechoMapaOutputDTO> getHechosMapa () {
        Specification<Hecho> spec = (root, query, cb) -> {
            return cb.and(
                    cb.isNotNull(root.get("ubicacion")),
                    cb.isNotNull(root.get("ubicacion").get("latitud")),
                    cb.isNotNull(root.get("ubicacion").get("longitud"))
            );
        };
        return this.hechosRepository.findAll(spec)
                .stream()
                .map(DTOConverter::convertirHechoMapaOutputDTO)
                .toList();
    }

    @Override
    public List<HechoOutputDTO> getHechosDestacados() {
        return this.hechosRepository.findHechoByDestacado(true)
                .stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .toList();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> setDestacadoHecho(Long idHecho, Boolean estaDestacado){
        Optional<Hecho> optionalHecho = hechosRepository.findById(idHecho);
        if (optionalHecho.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Hecho hecho = optionalHecho.get();
        hecho.setDestacado(estaDestacado); // Hibernate hará el update al hecho al ser transaccional
        return ResponseEntity.ok().build();
    }


    @Transactional
    @Override
    public void actualizarHechosRepository(List<Hecho> hechosActualizados) {
        final long t0 = System.currentTimeMillis();
        logger.info("Se van a persistir {} hechos", hechosActualizados.size());

        // 1) categorías
        this.categoriaService.cargarCategoriasHechos(hechosActualizados);
        logger.info("Categorías listas en {} ms", System.currentTimeMillis() - t0);

        /* TODO GEOREFF CAMBIO, OTRA API, LO COMENTAMOS X AHORA
        // 2) preparar ubicaciones y filtrar nulos
        List<Ubicacion> ubicaciones = hechosActualizados.stream()
                .map(Hecho::getUbicacion)
                .filter(Objects::nonNull)        // evitá NPE
                .toList();

        // 3) geolocalización por lotes
        logger.info("Geolocalizando {} ubicaciones en batch...", ubicaciones.size());
        long tg0 = System.currentTimeMillis();
        try {
            // bloqueamos aquí para tener tod0 georreferenciado antes de persistir
            geolocalizador.geolocalizarBatchAsync(ubicaciones).block();
        } catch (Exception e) {
            logger.error("Fallo geolocalizando en batch: {}", e.getMessage(), e);
            // si esto falla, seguimos con lo que tengamos (las ubicaciones quedaron como estaban)
        }
        logger.info("Geolocalización finalizada en {} ms", System.currentTimeMillis() - tg0);
        */

        // 4) persistir en batches para no saturar la BD
        logger.info("Persistiendo {} hechos...", hechosActualizados.size());
        final int BATCH_DB = 1000;
        int from = 0;
        while (from < hechosActualizados.size()) {
            int to = Math.min(from + BATCH_DB, hechosActualizados.size());
            List<Hecho> slice = hechosActualizados.subList(from, to);
            this.hechosRepository.saveAll(slice);
            from = to;
            logger.info("Persistidos {}/{}", to, hechosActualizados.size());
        }

        logger.info("Persistencia terminada en {} ms (total {} ms)",
                //(System.currentTimeMillis() - tg0),
                (System.currentTimeMillis() - 0L),
                (System.currentTimeMillis() - t0));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> agregarEtiquetaHecho(Long hechoId, String etiqueta){
        if (etiqueta == null || etiqueta.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Hecho hechoModificado = hechosRepository.getHechoById(hechoId);
        if (hechoModificado == null){
            return ResponseEntity.notFound().build();
        }
        Etiqueta nuevaEtiqueta = etiquetaService.verificarEtiqueta(etiqueta);
        hechoModificado.agregarEtiqueta(nuevaEtiqueta);
        return ResponseEntity.ok().build(); //TODO si se quiere que sea created se debe pasar una URL
    }

    @Override
    @Transactional
    public ResponseEntity<Void> eliminarEtiquetaHecho(Long hechoId, String etiqueta){
        if (etiqueta == null || etiqueta.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Hecho hechoModificado = hechosRepository.getHechoById(hechoId);
        if (hechoModificado == null){
            return ResponseEntity.notFound().build();
        }
        List<Etiqueta> etiquetasHecho = hechoModificado.getEtiquetas();
        Optional<Etiqueta> etiquetaEliminada = etiquetasHecho.stream().filter(e -> e.getNombre().equals(etiqueta)).findFirst();
        if (etiquetaEliminada.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        etiquetaEliminada.ifPresent(hechoModificado::eliminarEtiqueta);
        return ResponseEntity.noContent().build();
    }

    // LOGGER
    private void logearHechosCargados(List<Hecho> hechos, String urlFuente){
        logger.info("Hechos cargados - Cantidad: {} - Fuente: {}", hechos.size(), urlFuente);
        hechos.forEach(hecho ->
                logger.info
                        ("Hecho cargado - ID: {} - Titulo: {} -  Descripción: {} -  Categoria: {} -  Fecha De Ocurrencia: {}"
                                , hecho.getId(), hecho.getTitulo(),hecho.getDescripcion(),hecho.getCategoria().getNombre(),hecho.getFechaDeOcurrencia()));
    }


    /*
    Root es la tabla principal a la que accedemos
    cb es "CriteriaBuilder", que crea los criterios para los request a la base de datos
    query es la consulta completa, sirve si necesitamos hacer joins, fetchs, cambiar orden, etc.
    Los "Predicate" son las consultas logicas que crea el criteriaBuilder
    */
    public Specification<Hecho> buildHechosSpecification(HechoFilter hechosFilter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hechosFilter.getCategoria() != null) {
                predicates.add(cb.equal(root.get("categoria").get("nombre"), hechosFilter.getCategoria() ));
            }
            if (hechosFilter.getFReporteDesde() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaDeCarga"), hechosFilter.getFReporteDesde()));
            }
            if (hechosFilter.getFReporteHasta() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaDeCarga"), hechosFilter.getFReporteHasta()));
            }
            if (hechosFilter.getFAconDesde() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaDeOcurrencia"), hechosFilter.getFAconDesde()));
            }
            if (hechosFilter.getFAconHasta() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaDeOcurrencia"), hechosFilter.getFAconHasta()));
            }
            if (hechosFilter.getFuenteId() != null) {
                predicates.add(cb.equal(root.get("fuente").get("id"), hechosFilter.getFuenteId()));
            }
            if (hechosFilter.getProvincia() != null) {
                predicates.add(cb.equal(root.get("ubicacion").get("provincia"), hechosFilter.getProvincia()));
            }
            if (hechosFilter.getEtiqueta() != null && !hechosFilter.getEtiqueta().isBlank()) {
                // Hacemos un JOIN entre Hecho y Etiqueta
                Join<Object, Object> joinEtiquetas = root.join("etiquetas", JoinType.INNER);
                predicates.add( cb.equal( cb.lower( joinEtiquetas.get("nombre") ), hechosFilter.getEtiqueta().toLowerCase() ) );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public List<Hecho> findByFuente(Fuente fuente){
        return this.hechosRepository.findAllByFuente(fuente);
    }

    public List<Hecho> findAllSpec(Specification<Hecho> spec, Pageable pageable){
        if (pageable == null){
            return this.hechosRepository.findAll(spec);
        } else {
            return this.hechosRepository.findAll(spec, pageable).getContent();
        }
    }
}