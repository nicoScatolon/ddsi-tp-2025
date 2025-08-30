package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.HechosFilterDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Etiqueta;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator.HechoComparator;
import ar.edu.utn.frba.dds.domain.entities.HechoFilter;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;
    private final ICategoriaService categoriaService;
    private final CriterioFactory criterioFactory;

    private static final Logger logger = LoggerFactory.getLogger(HechosService.class);

    public HechosService(IHechosRepository hechosRepository, ICategoriaService categoriaService, CriterioFactory criterioFactory, IEtiquetasService etiquetaService) {
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
        Hecho hecho = this.hechosRepository.findById(id);
        return DTOConverter.convertirHechoOutputDTO(hecho);
    }

    @Override
    public Hecho findEntidadPorId(Long id){
        return this.hechosRepository.findById(id);
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

        List<ICriterio> criterios = this.criterioFactory.crearCriteriosParametros(categoriaEntidad, hechosFilter);

        // Si no hay criterios, devolver todos los hechos
        if (criterios.isEmpty()){
            return findAllOutput();
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

        hechosActualizados.forEach(n -> n.setCategoria(categoriaService.agregarCategoria(n.getCategoria())) );
        this.hechosRepository.saveAll(hechosActualizados);
    }

    public void configurarComparacion(){
        HechoComparator comparator = HechoComparator.getInstance();
        //comparator.agregarComando(comando1);
        //comparator.eliminarComando(comando2);
        //TODO
    }

    public ResponseEntity<Void> agregarEtiquetaHecho(Long hechoId, String etiqueta){
        if (etiqueta == null || etiqueta.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Hecho hechoModificado = hechosRepository.findById(hechoId);
        if (hechoModificado == null){
            return ResponseEntity.notFound().build();
        }
        Etiqueta nuevaEtiqueta = new Etiqueta(etiqueta);
        hechoModificado.agregarEtiqueta(nuevaEtiqueta);
        return ResponseEntity.ok().build(); //TODO si se quiere que sea created se debe pasar una URL
    }

    public ResponseEntity<Void> eliminarEtiquetaHecho(Long hechoId, String etiqueta){
        if (etiqueta == null || etiqueta.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Hecho hechoModificado = hechosRepository.findById(hechoId);
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