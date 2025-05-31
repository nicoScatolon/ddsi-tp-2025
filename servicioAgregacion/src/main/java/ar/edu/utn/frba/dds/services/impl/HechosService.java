package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;
import ar.edu.utn.frba.dds.domain.entities.Hecho.impl.HechoFuenteProxy;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import ar.edu.utn.frba.dds.services.IFuentesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class HechosService implements IHechosService {
    private static final Logger logger = LoggerFactory.getLogger(HechosService.class);



    public HechosService(IHechosRepository hechosRepository, IFuentesService fuentesService ,ICategoriaService categoriaService, WebClient.Builder webClientBuilder) {
        this.hechosRepository = hechosRepository;
        this.webClientBuilder = webClientBuilder;
        this.categoriasRepository = categoriasRepository;
    }

    @Override
    public void actualizarHechosScheduler() {
        List <TipoFuente> listaTipos = new ArrayList<TipoFuente>();
        listaTipos.add(TipoFuente.ESTATICA);
        listaTipos.add(TipoFuente.DINAMICA);
        List<Fuente> fuentes = fuentesService.buscarFuentePorTipo(listaTipos);
        //TODO ver como setearlo en las properties
    }

    @Override
    public List<HechoBase> obtenerHechosProxy(){
        List<Fuente> fuentes = fuentesService.buscarFuentePorTipo(TipoFuente.PROXY);
        List<HechoBase> hechoBases = new ArrayList<>();

        for (Fuente fuente : fuentes){
            List<IHechoInputDTO> hechoInputDTOS = this.obtenerHechosFuente(fuente);
            List<HechoBase> hechosListos = this.convertirHechosDTO(hechoInputDTOS);
            hechoBases.addAll(hechosListos);
        }
        return hechoBases;
    }

    @Override
    public List<HechoOutputDTO> findAll(){
        return this.hechosRepository
                .findAll()
                .stream()
                .map(DTOConverter::convertirHechoOutputDTO)
                .toList();
    }

    @Override
    public HechoOutputDTO findByID(Long id) {
        HechoBase hecho = this.hechosRepository.findById(id);
        return DTOConverter.convertirHechoOutputDTO(hecho);
    }

    private List<IHechoInputDTO> obtenerHechosFuente(Fuente fuente){
        List<IHechoInputDTO> hechoInputDTOS;
        hechoInputDTOS = fuente.getHechos(webClientBuilder);
        return hechoInputDTOS;
    }

    private List<HechoBase> convertirHechosDTO (List<IHechoInputDTO> iHechoInputDTOS){
        List<HechoBase> hechosListos = new ArrayList<>();
        for (IHechoInputDTO hechoInputDTO : iHechoInputDTOS) {
            HechoBase hecho = DTOConverter.convertirHechoInputDTO(hechoInputDTO);
            Categoria categoriaPersistida = categoriaService.agregarCategoria(hechoInputDTO.getNombreCategoria());
            hecho.setCategoria(categoriaPersistida);
            hechosListos.add(hecho);
        }
        return hechosListos;
    }

    private void actualizarHechos(Fuente fuente) {
        List<IHechoInputDTO> hechoInputDTOS = this.obtenerHechosFuente(fuente);

        List<HechoBase> hechosListos = this.convertirHechosDTO(hechoInputDTOS);
        if (hechosListos.isEmpty()) {
            logger.info("No se encontraron hechos para actualizar.");
            return;
        }
        hechosRepository.saveAll(hechosListos);
        this.logearHechosCargados(hechosListos, fuente.getUrl());
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