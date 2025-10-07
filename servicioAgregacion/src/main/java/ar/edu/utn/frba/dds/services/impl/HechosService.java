package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.HechosFilterDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoMapaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.Criterio;
import ar.edu.utn.frba.dds.domain.entities.Etiqueta;
import ar.edu.utn.frba.dds.domain.entities.Geolocalizadores.Georef;
import ar.edu.utn.frba.dds.domain.entities.Geolocalizadores.IGeoLocalizador;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator.HechoComparator;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator.IComandComparator;
import ar.edu.utn.frba.dds.domain.entities.HechoFilter;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.services.IEtiquetasService;
import ar.edu.utn.frba.dds.services.IHechosService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.Objects;
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
        Categoria categoriaEntidad = null; //la inicializo en null
        //Verifico si la categoria existe
        if (hechosFilter.getCategoria() != null){
            categoriaEntidad = categoriaService.findByNombre(hechosFilter.getCategoria());
            if (categoriaEntidad == null){
                hechosFilter.setCategoria(null);
            }
        }
        List<Criterio> criterios = this.criterioFactory.crearCriteriosParametros(categoriaEntidad, hechosFilter);

        if (filterDTO.getPage() == null) { // no tiene pagina -> devuelvo tod0s los hechos
            if (criterios.isEmpty()) {
                return this.findAllOutput();
            } else {
                return this.findAll().stream()
                        .filter(h -> criterios.stream().allMatch(c -> c.pertenece(h)))
                        .map(DTOConverter::convertirHechoOutputDTO)
                        .toList();
            }
        }

        Pageable pageable = PageRequest.of(filterDTO.getPage(), pageSize);

        Page<Hecho> hechosPagina = hechosRepository.findAll(pageable);

        if (criterios.isEmpty()) {
            return hechosPagina.getContent().stream()
                    .map(DTOConverter::convertirHechoOutputDTO)
                    .toList();
        } else {
            return hechosPagina.getContent().stream()
                    .filter(h -> criterios.stream().allMatch(c -> c.pertenece(h)))
                    .map(DTOConverter::convertirHechoOutputDTO)
                    .toList();
        }
    }

    @Override
    public List<HechoMapaOutputDTO> getHechosMapa () {
        return this.findAll().stream()
                .map(DTOConverter::convertirHechoMapaOutputDTO)
                .toList();
    }

    @Transactional
    @Override
    public void actualizarHechosRepository(List<Hecho> hechosActualizados) {
        final long t0 = System.currentTimeMillis();
        logger.info("Se van a persistir {} hechos", hechosActualizados.size());

        // 1) categorías
        this.categoriaService.cargarCategoriasHechos(hechosActualizados);
        logger.info("Categorías listas en {} ms", System.currentTimeMillis() - t0);

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
                (System.currentTimeMillis() - tg0),
                (System.currentTimeMillis() - t0));
    }

    //TODO hacer configuracion del comparator por datos del properties

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
}