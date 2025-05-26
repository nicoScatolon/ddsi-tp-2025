package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class HechosService implements IHechosService {
    private static final Logger logger = LoggerFactory.getLogger(HechosService.class);
    private final IHechosRepository hechosRepository;
    private final ICategoriasRepository categoriasRepository;
    private final WebClient.Builder webClientBuilder;

    public HechosService(IHechosRepository hechosRepository, ICategoriasRepository categoriasRepository, WebClient.Builder webClientBuilder) {
        this.hechosRepository = hechosRepository;
        this.webClientBuilder = webClientBuilder;
        this.categoriasRepository = categoriasRepository;
    }

    public List<IHechoInputDTO> recolectarHechos(String fuenteURL) {
        try {
            WebClient webClient = webClientBuilder.baseUrl(fuenteURL).build();
            return webClient.get()
                    .uri("/hechos")
                    .retrieve()
                    .bodyToFlux(IHechoInputDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            logger.error("Error al recolectar hechos desde la fuente: {}", fuenteURL, e);
            return List.of();
        }
    }

    @Override
    public void actualizarHechosFuente(String fuenteURL) {
        List<IHecho> hechosConFuente =
                this.recolectarHechos(fuenteURL)
                        .stream()
                        .map(MapperHechos::convertirHechoInputDTO)
                        .toList(); //ToDO revisar

        if (hechosConFuente.isEmpty()) {
            logger.warn("No se encontraron hechos desde la fuente: {}", fuenteURL);
        } else {
            this.actualizarRepositoryHecho(hechosConFuente);
            this.logearHechosCargados(hechosConFuente, fuenteURL);
        }
    }

    @Override
    public List<HechoOutputDTO> findAll(){
        return this.hechosRepository
                .findAll()
                .stream()
                .map(MapperHechos::convertirHechoOutputDTO)
                .toList();
    }

    @Override
    public HechoOutputDTO findByID(Long id) {
        IHecho hecho = this.hechosRepository.findById(id);
        return MapperHechos.convertirHechoOutputDTO(hecho);
    }

    // LOGGER
    @Override
    public void logearHechosCargados(List<IHecho> hechos, String urlFuente){
        logger.info("Hechos cargados - Cantidad: {} - Fuente: {}", hechos.size(), urlFuente);
        hechos.forEach(hecho ->
                logger.info
                        ("Hecho cargado - ID: {} - Titulo: {} -  Descripción: {} -  Categoria: {} -  Fecha De Ocurrencia: {}"
                        , hecho.getId(), hecho.getTitulo(),hecho.getDescripcion(),hecho.getCategoria().getNombre(),hecho.getFechaDeOcurrencia()));
    }

    private void actualizarRepositoryHecho(List<IHecho> hechos) {
        for (IHecho hecho : hechos) {
            Categoria categoriaPersistida = categoriasRepository.save(hecho.getCategoria());
            hecho.setCategoria(categoriaPersistida);
        }
        hechosRepository.saveAll(hechos);
    }
}