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
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.services.IEtiquetasService;
import ar.edu.utn.frba.dds.services.IGeorefService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;
    private final ICategoriaService categoriaService;
    private final IEtiquetasService etiquetaService;

    private final IGeoLocalizador geolocalizador;

    private static final Logger logger = LoggerFactory.getLogger(HechosService.class);

    @Value("${app.pagination.hechos.size}") // 20 es el valor por defecto si no está definido
    private int pageSize;

    public HechosService(IHechosRepository hechosRepository,
                         ICategoriaService categoriaService,
                         IEtiquetasService etiquetaService,
                         IGeorefService georefService) {
        this.hechosRepository = hechosRepository;
        this.categoriaService = categoriaService;
        this.etiquetaService = etiquetaService;

        this.geolocalizador = new Georef(georefService);
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
    public List<HechoOutputDTO> getHechos(HechosFilterDTO filterDTO, Boolean fueEliminado){
        HechoFilter hechosFilter = DTOConverter.convertirHechoFilterInputDTO(filterDTO);

        if (filterDTO.getPage() == null) { // no tiene pagina -> devuelvo tod0s los hechos
            return this.hechosRepository.findAll(buildHechosSpecification(hechosFilter, fueEliminado))
                    .stream()
                    .map(DTOConverter::convertirHechoOutputDTO)
                    .toList();
        }

        Pageable pageable = PageRequest.of(filterDTO.getPage(), pageSize);

        return this.hechosRepository.findAll(buildHechosSpecification(hechosFilter, fueEliminado), pageable)
                .stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .toList();
    }

    @Override
    public List<HechoMapaOutputDTO> getHechosMapa() {
        return hechosRepository.findAllMapaDTO(); // ya filtra solo los que tienen lat/lng
    }

    @Override
    public List<HechoMapaOutputDTO> getHechosMapaPorProvincia(String provincia) {
        return hechosRepository.findAllPorProvinciaDTO(provincia);
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

        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        // 1) Asegurar fechaDeCarga y fechaDeOcurrencia no nulas
        for (Hecho h : hechosActualizados) {
            if (h.getFechaDeCarga() == null) {
                h.setFechaDeCarga(now);
            }
            if (h.getFechaDeOcurrencia() == null) { // fallback si viene null
                h.setFechaDeOcurrencia(h.getFechaDeCarga());
            }
        }

        // 2) Log de trazabilidad
        long nulos = hechosActualizados.stream()
                .filter(h -> h.getFechaDeOcurrencia() == null)
                .peek(h -> logger.warn("Hecho sin fechaDeOcurrencia. origenId={}, titulo={}",
                        h.getOrigenId(), h.getTitulo()))
                .count();
        if (nulos > 0) logger.warn("Hechos sin fechaDeOcurrencia detectados: {}", nulos);

        // 3) Cargar categorías
        this.categoriaService.cargarCategoriasHechos(hechosActualizados);
        logger.info("Categorías listas en {} ms", System.currentTimeMillis() - t0);

        // 4) Preparar ubicaciones
        List<Ubicacion> ubicaciones = hechosActualizados.stream()
                .map(Hecho::getUbicacion)
                .filter(Objects::nonNull)
                .toList();

        List<Ubicacion> ubicacionesSinProvincia = ubicaciones.stream()
                .filter(u -> u.getProvincia() == null || u.getProvincia().isBlank())
                .toList();

        logger.info("Total ubicaciones: {}, sin provincia: {}",
                ubicaciones.size(), ubicacionesSinProvincia.size());

        // 5) Geolocalización solo si hace falta
        if (!ubicacionesSinProvincia.isEmpty()) {
            logger.info("Geolocalizando {} ubicaciones...", ubicacionesSinProvincia.size());
            try {
                geolocalizador.geolocalizarBatchAsync(ubicacionesSinProvincia)
                        .timeout(Duration.ofMinutes(10))
                        .block();
                logger.info("Geolocalización completada");
            } catch (Exception e) {
                logger.error("Error en geolocalización batch: {}", e.getMessage());
            }
        } else {
            logger.info("Todas las ubicaciones ya tienen provincia, saltando geolocalización");
        }

        // 6) Persistir hechos por batches
        logger.info("Persistiendo {} hechos...", hechosActualizados.size());
        final int BATCH_DB = 1000;
        int from = 0;
        while (from < hechosActualizados.size()) {
            int to = Math.min(from + BATCH_DB, hechosActualizados.size());
            List<Hecho> slice = hechosActualizados.subList(from, to);

            // redundante pero seguro
            for (Hecho h : slice) {
                if (h.getFechaDeCarga() == null) {
                    h.setFechaDeCarga(now);
                }
                if (h.getFechaDeOcurrencia() == null) {
                    h.setFechaDeOcurrencia(h.getFechaDeCarga());
                }
            }

            try {
                this.hechosRepository.saveAll(slice);
                logger.info("Persistidos {}/{}", to, hechosActualizados.size());
            } catch (Exception e) {
                logger.error("Error persistiendo batch {}-{}: {}", from, to, e.getMessage());
                // rollback defensivo
                org.springframework.transaction.interceptor.TransactionAspectSupport
                        .currentTransactionStatus().setRollbackOnly();
                return; // corta para evitar flush sucio
            }

            from = to;
        }

        logger.info("Persistencia terminada en {} ms", System.currentTimeMillis() - t0);
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
        return ResponseEntity.ok().build();
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
    public Specification<Hecho> buildHechosSpecification(HechoFilter hechosFilter, Boolean fueEliminado) {
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

            if (fueEliminado != null) {
                predicates.add(cb.equal(root.get("fueEliminado"), fueEliminado));
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

    @Override
    public ResponseEntity<Void> agregarEtiquetasHecho(Long id, List<String> etiquetas) {
        if (etiquetas == null || etiquetas.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Hecho hecho = hechosRepository.getHechoById(id);
        if (hecho == null) {
            return ResponseEntity.notFound().build();
        }

        List<Etiqueta> etiquetasVerificadas = new ArrayList<>();
        for (String nombreEtiqueta : etiquetas) {
            if (nombreEtiqueta != null && !nombreEtiqueta.isBlank()) {
                Etiqueta etiqueta = etiquetaService.verificarEtiqueta(nombreEtiqueta);
                etiquetasVerificadas.add(etiqueta);
            }
        }

        if (etiquetasVerificadas.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        hecho.modificarEtiquetas(etiquetasVerificadas);

        hechosRepository.save(hecho);

        return ResponseEntity.ok().build();
    }
}