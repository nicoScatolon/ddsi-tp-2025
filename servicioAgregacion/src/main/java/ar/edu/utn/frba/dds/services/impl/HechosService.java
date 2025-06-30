package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;
    private final ICategoriaService categoriaService;
    private final CriterioFactory criterioFactory;

    private static final Logger logger = LoggerFactory.getLogger(HechosService.class);

    public HechosService(IHechosRepository hechosRepository, ICategoriaService categoriaService, CriterioFactory criterioFactory) {
        this.hechosRepository = hechosRepository;
        this.categoriaService = categoriaService;
        this.criterioFactory = criterioFactory;
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
    public List<HechoOutputDTO> getHechos(CategoriaInputDTO catDTO, LocalDateTime fReporteDesde, LocalDateTime fReporteHasta, LocalDate fAconDesde, LocalDate fAconHasta, UbicacionInputDTO ubiDTO){
        Categoria categoria = DTOConverter.categoriaInputDTO(catDTO);
        if (categoriaService.findByNombre(categoria.getNombre()) == null) {categoria = null;}
        //verificar si categoria existe
        Ubicacion ubicacion = DTOConverter.convertirUbicacion(ubiDTO);
        List<ICriterio> criterios = this.criterioFactory.crearCriteriosParametros(categoria,fReporteDesde,fReporteHasta,fAconDesde,fAconHasta,ubicacion);

        if (criterios.isEmpty() || criterios == null){
            return findAllOutput();
        } else {
            return this.findAll().stream()
                    .filter(h -> criterios.stream().allMatch(c -> c.pertenece(h)))
                    .map(DTOConverter::convertirHechoOutputDTO)
                    .toList();
        }
    }

    public void actualizarHechosRepository(List<Hecho> hechosActualizados){
        // el hecho ya viene con una categoria que puede o no existir -> es temporal y no esta asociada al repo
        // la idea es enviarla
        hechosActualizados.forEach(n -> n.setCategoria(categoriaService.agregarCategoria(n.getCategoria())) );
        this.hechosRepository.saveAll(hechosActualizados);
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