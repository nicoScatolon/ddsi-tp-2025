package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteAdapter;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.services.IFuentesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;

    private final IFuentesService fuentesService;
    private final ICategoriaService categoriaService;

    private static final Logger logger = LoggerFactory.getLogger(HechosService.class);

    public HechosService(IHechosRepository hechosRepository, IFuentesService fuentesService ,ICategoriaService categoriaService) {
        this.hechosRepository = hechosRepository;
        this.categoriaService = categoriaService;
        this.fuentesService = fuentesService;
    }

    @Override
    public void actualizarHechosScheduler() {
        List<TipoFuente> listaTipos = new ArrayList<>();
        listaTipos.add(TipoFuente.DINAMICA);
        listaTipos.add(TipoFuente.ESTATICA);
        List<IFuente> fuentes = fuentesService.buscarFuentePorTipo(listaTipos);

        for (IFuente fuente : fuentes){
            boolean resultado = actualizarHechos(fuente);
            if (resultado){
                //las colecciones que tengan esa fuente deben ser actualizadas
                //TODO setear la actualizacion de las colecciones
            }
        }
        //TODO ver como setearlo en las properties
    }

    @Override
    public List<Hecho> consumirFuente(IFuente fuente) {
        if (fuente.getTipo() != TipoFuente.PROXY){
            throw new RuntimeException("Tipo de fuente incorrecto");
        }
        return obtenerHechosFuente(fuente);
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

    private List<Hecho> obtenerHechosFuente(IFuente fuente){
        List<Hecho> hechosFuente;
        FuenteAdapter adapterConcreto = fuente.getTipo().crearAdapter();
        adapterConcreto.setFuente(fuente);
        hechosFuente = adapterConcreto.obtenerHechosFuente();
        return hechosFuente;
    }

    private boolean actualizarHechos(IFuente fuente) {
        List<Hecho> hechosListos = this.obtenerHechosFuente(fuente);
        if (hechosListos.isEmpty()) {
            logger.info("No se encontraron hechos para actualizar.");
            return false;
        }
        this.guardarHechosRepository(hechosListos);
        this.logearHechosCargados(hechosListos, fuente.getUrl());
        return true;
    }

    public void guardarHechosRepository(List<Hecho> hechos){ //Util para los test
        for (Hecho hechoActual : hechos) {
            hechoActual.setCategoria( categoriaService.agregarCategoria( hechoActual.getCategoria() ) );
        }
        hechosRepository.saveAll(hechos);
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