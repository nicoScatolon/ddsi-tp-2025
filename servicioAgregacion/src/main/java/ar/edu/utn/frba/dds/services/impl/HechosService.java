package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.HechosFilterDTO;
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
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.services.IEtiquetasService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

        // Si no hay criterios, devolver todos los hechos
        if (criterios.isEmpty()){
            return this.findAllOutput();
        } else {
            //Filtrar por criterios
            return this.findAll().stream()
                    .filter(h -> criterios.stream().allMatch(c -> c.pertenece(h)))
                    .map(DTOConverter::convertirHechoOutputDTO)
                    .toList();
        }
    }

    @Override
    public void actualizarHechosRepository(List<Hecho> hechosActualizados){
        // el hecho ya viene con una categoria que puede o no existir -> es temporal y no esta asociada al repo
        // la idea es enviarla
        this.categoriaService.cargarCategoriasHechos(hechosActualizados);
        //hechosActualizados.forEach(h -> h.setUbicacion( geolocalizador.geolocalizar(h.getUbicacion()) ));
        this.hechosRepository.saveAll(hechosActualizados);
    }

    public void configurarComparacion(List<IComandComparator> comandos){
        HechoComparator comparator = HechoComparator.getInstance();
        comparator.setListaComandos(comandos);
        //TODO para conectarse a front deberiamos asociar enums/strings con cada comando para que se puedan ver por pantalla,
        //  y recibiriamos eso por la conexion no el comando en si
        //TODO hacerlo por properties y listo
    }

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