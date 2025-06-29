package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;
    private final ICategoriaService categoriaService;

    private static final Logger logger = LoggerFactory.getLogger(HechosService.class);

    public HechosService(IHechosRepository hechosRepository, ICategoriaService categoriaService) {
        this.hechosRepository = hechosRepository;
        this.categoriaService = categoriaService;
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

    public void guardarHechosRepository(List<Hecho> hechos){ //Util para los test
        hechosRepository.saveAll(hechos);
    }

    public void actualizarHechos(List<Hecho> hechosActualizados){

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