package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.services.IFuentesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;

    private final IFuentesService fuentesService;
    private final ICategoriaService categoriaService;

    private final WebClient.Builder webClientBuilder;
    private static final Logger logger = LoggerFactory.getLogger(HechosService.class);

    public HechosService(IHechosRepository hechosRepository, IFuentesService fuentesService ,ICategoriaService categoriaService, WebClient.Builder webClientBuilder) {
        this.hechosRepository = hechosRepository;
        this.webClientBuilder = webClientBuilder;
        this.categoriaService = categoriaService;
        this.fuentesService = fuentesService;
    }

    @Override
    public void actualizarHechosScheduler() {
        List <TipoFuente> listaTipos = new ArrayList<>();
        listaTipos.add(TipoFuente.ESTATICA);
        listaTipos.add(TipoFuente.DINAMICA);
        List<Fuente> fuentes = fuentesService.buscarFuentePorTipo(listaTipos);
        for (Fuente fuente : fuentes){
            boolean resultado = actualizarHechos(fuente);
            if (resultado){
                //las colecciones que tengan esa fuente deben ser actualizadas
            }
        }
        //TODO ver como setearlo en las properties
    }

    @Override
    public List<HechoBase> obtenerHechosProxy(){
        List<Fuente> fuentes = fuentesService.buscarFuentePorTipo(TipoFuente.PROXY);
        List<HechoBase> hechoBases = new ArrayList<>();

        for (Fuente fuente : fuentes){
            List<IHechoInputDTO> hechoInputDTOS = this.obtenerHechosFuente(fuente);
            List<HechoBase> hechosListos = this.convertirHechosDTO(hechoInputDTOS, fuente.getId());
            hechoBases.addAll(hechosListos);
        }
        return hechoBases;
    }

    @Override
    public List<HechoBase> findAll(){ return this.hechosRepository.findAll(); }

    @Override
    public List<HechoOutputDTO> findAllOutput(){
        return this.findAll()
                .stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .toList();
    }

    @Override
    public HechoOutputDTO findByID(Long id) {
        HechoBase hecho = this.hechosRepository.findById(id);
        return DTOConverter.convertirHechoOutputDTO(hecho);
    }

    @Override
    public List<HechoBase> findByTipoFuente(List<TipoFuente> tiposFuentes){
        List<HechoBase> hechoBuscados = new ArrayList<>();
        //si no tiene fuentes asociadas devolvemos una lista vacía
        if (tiposFuentes.isEmpty()) return hechoBuscados;
        //obtenemos id de las fuentes que sean de los tipos buscados
        List<Long> idsFuentesBuscadas = fuentesService.buscarFuentePorTipo(tiposFuentes).stream().map(Fuente::getId).toList();
        //filtramos los hechos que tengan algún id que matchee con los de las fuentes encontradas
        hechoBuscados = this.hechosRepository.findAll().stream().filter(h -> idsFuentesBuscadas.contains(h.getId())).toList();
        return hechoBuscados;
    }

    private List<IHechoInputDTO> obtenerHechosFuente(Fuente fuente){
        List<IHechoInputDTO> hechoInputDTOS;
        hechoInputDTOS = fuente.getHechos(webClientBuilder);
        return hechoInputDTOS;
    }

    private List<HechoBase> convertirHechosDTO (List<IHechoInputDTO> iHechoInputDTOS, Long idFuente){
        List<HechoBase> hechosListos = new ArrayList<>();
        for (IHechoInputDTO hechoInputDTO : iHechoInputDTOS) {
            HechoBase hecho = DTOConverter.convertirHechoInputDTO(hechoInputDTO,idFuente);
            Categoria categoriaPersistida = categoriaService.agregarCategoria(hechoInputDTO.getCategoria());
            hecho.setCategoria(categoriaPersistida);
            hechosListos.add(hecho);
        }
        return hechosListos;
    }

    private boolean actualizarHechos(Fuente fuente) {
        List<IHechoInputDTO> hechoInputDTOS = this.obtenerHechosFuente(fuente);

        List<HechoBase> hechosListos = this.convertirHechosDTO(hechoInputDTOS, fuente.getId());
        if (hechosListos.isEmpty()) {
            logger.info("No se encontraron hechos para actualizar.");
            return false;
        }
        hechosRepository.saveAll(hechosListos);
        this.logearHechosCargados(hechosListos, fuente.getUrl());
        return true;
    }

    // LOGGER
    private void logearHechosCargados(List<HechoBase> hechos, String urlFuente){
        logger.info("Hechos cargados - Cantidad: {} - Fuente: {}", hechos.size(), urlFuente);
        hechos.forEach(hecho ->
                logger.info
                        ("Hecho cargado - ID: {} - Titulo: {} -  Descripción: {} -  Categoria: {} -  Fecha De Ocurrencia: {}"
                        , hecho.getId(), hecho.getTitulo(),hecho.getDescripcion(),hecho.getCategoria().getNombre(),hecho.getFechaDeOcurrencia()));
    }
}