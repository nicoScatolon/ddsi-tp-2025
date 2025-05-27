package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputDinamicaDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputEstaticaDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputProxyDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
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
    private final IHechosRepository hechosRepository;
    private final ICategoriasRepository categoriasRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${app.fuenteDinamica-url}")
    private String urlDinamica;

    @Value("${app.fuenteProxy-url}")
    private String urlProxy;

    @Value("${app.fuenteEstatica-url}")
    private String urlEstatica;

    public HechosService(IHechosRepository hechosRepository, ICategoriasRepository categoriasRepository, WebClient.Builder webClientBuilder) {
        this.hechosRepository = hechosRepository;
        this.webClientBuilder = webClientBuilder;
        this.categoriasRepository = categoriasRepository;
    }

    @Override
    public void actualizarHechosScheduler() {
        actualizarHechos(urlEstatica,1);
        actualizarHechos(urlDinamica,2);
    }

    @Override
    public void actualizarHechosManualmente(){
        actualizarHechos(urlEstatica,1);
        actualizarHechos(urlDinamica,2);
        actualizarHechos(urlProxy,3);
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

    // LOGGER
    private void logearHechosCargados(List<HechoBase> hechos, String urlFuente){
        logger.info("Hechos cargados - Cantidad: {} - Fuente: {}", hechos.size(), urlFuente);
        hechos.forEach(hecho ->
                logger.info
                        ("Hecho cargado - ID: {} - Titulo: {} -  Descripción: {} -  Categoria: {} -  Fecha De Ocurrencia: {}"
                        , hecho.getId(), hecho.getTitulo(),hecho.getDescripcion(),hecho.getCategoria().getNombre(),hecho.getFechaDeOcurrencia()));
    }

    // MÉTODOS PRIVADOS
    public void actualizarRepositoryHecho(List<HechoBase> hechos) {
        for (HechoBase hecho : hechos) {
            Categoria categoriaPersistida = categoriasRepository.save(this.StringToHandle(hecho.getCategoria().getNombre()));
            hecho.setCategoria(categoriaPersistida);
        }
        hechosRepository.saveAll(hechos);
    }

    private void actualizarHechos(String url, Integer fuenteID) {
        List<? extends IHechoInputDTO> hechoInputDTOS;

        switch (fuenteID) {
            case 1 -> hechoInputDTOS = this.recuperarHechosEstatica();
            case 2 -> hechoInputDTOS = this.recuperarHechosDinamica();
            case 3 -> hechoInputDTOS = this.recuperarHechosProxy();
            default -> {
                logger.warn("Identificador de fuente erróneo: {}", fuenteID);
                return;
            }
        }

        List<HechoBase> hechos = new ArrayList<>(hechoInputDTOS.stream()
                .map(DTOConverter::convertirHechoInputDTO)
                .toList());

        if (hechos.isEmpty()) {
            logger.warn("No se encontraron hechos desde la fuente: {}", url);
        } else {
            this.actualizarRepositoryHecho(hechos);
            this.logearHechosCargados(hechos, url);
        }
    }

    private List<HechoInputDinamicaDTO> recuperarHechosDinamica() {
        try {
            WebClient webClient = webClientBuilder.baseUrl(urlDinamica).build();
            return webClient.get()
                    .uri("/hechos")
                    .retrieve()
                    .bodyToFlux(HechoInputDinamicaDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            logger.error("Error al recolectar hechos desde la fuente: {}", urlDinamica, e);
            return List.of();
        }
    }

    private List<HechoInputEstaticaDTO> recuperarHechosEstatica() {
        try {
            WebClient webClient = webClientBuilder.baseUrl(urlEstatica).build();
            return webClient.get()
                    .uri("/hechos")
                    .retrieve()
                    .bodyToFlux(HechoInputEstaticaDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            logger.error("Error al recolectar hechos desde la fuente: {}",urlDinamica , e);
            return List.of();
        }
    }

    private List<HechoInputProxyDTO> recuperarHechosProxy() {
        try {
            WebClient webClient = webClientBuilder.baseUrl(urlProxy).build();
            return webClient.get()
                    .uri("/hechos")
                    .retrieve()
                    .bodyToFlux(HechoInputProxyDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            logger.error("Error al recolectar hechos desde la fuente: {}",urlDinamica , e);
            return List.of();
        }
    }

    private String StringToHandle(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("El string no puede ser nulo ni vacío.");
        }

        return string
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")  // reemplaza caracteres no alfanuméricos por guiones
                .replaceAll("^-+|-+$", "");     // quita guiones al inicio o final
    }
}