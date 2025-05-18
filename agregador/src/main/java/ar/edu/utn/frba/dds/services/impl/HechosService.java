package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputResponse;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho;
import ar.edu.utn.frba.dds.domain.repository.ICategoriaRepository;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class HechosService implements IHechosService {
    private IHechosRepository hechosRepository;
    private ICategoriaRepository categoriaRepository;  //Necesario únicamente en el caso de que no nos envíen una categoria.
    private WebClient webClient;

    @Value("${app.base-url}") //ToDO: todavia no entiendo como es la comunicacion entre modulos
    private String baseUrl;

    public HechosService(IHechosRepository hechosRepository,
                         ICategoriaRepository categoriaRepository,
                         WebClient.Builder webClientBuilder) {
        this.hechosRepository = hechosRepository;
        this.categoriaRepository = categoriaRepository;
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public List<Hecho> obtenerTodosLasHechos() {
        return webClient
                .get()
                .uri("/hechos")
                .retrieve()
                .bodyToMono(HechoInputResponse.class)
                .map(resp -> resp.getHechoInputDTOs().stream()
                        .map(this::hecho)
                        .toList())
                .block();
    }

    public List<HechoOutputDTO> buscarTodosLosHechos(){
        //ToDO, si es Admin, aquí se debería verificar
        return this.hechosRepository
                .findAll()
                .stream()
                .map(this::hechoOutputDTO)
                .toList();
    }

    @Override
    public HechoOutputDTO crearHecho(HechoInputDTO hechoInputDTO) {
        //ToDO, si es Admin, aquí se debería verificar
        Hecho hecho = Hecho.builder()
                .titulo(hechoInputDTO.getTitulo())
                .descripcion(hechoInputDTO.getDescripcion())
                .ubicacion(hechoInputDTO.getUbicacion())
                .fechaDeOcurrencia(hechoInputDTO.getFechaDeOcurrencia())
                .categoria(hechoInputDTO.getCategoria())
                //ToDO: hay q revisar las fuentes que nos envian. Porq si nos envian una categoria está bien, si nos envian un hash y string nombre hay q modificarlo.
                .build();

        this.hechosRepository.save(hecho);
        return this.hechoOutputDTO(hecho);
    }

    @Override
    public HechoOutputDTO buscarHechoPorId(Long id) {
        Hecho hecho = this.hechosRepository.findById(id);
        return this.hechoOutputDTO(hecho);
    }

    private HechoOutputDTO hechoOutputDTO(Hecho hecho) {
        return HechoOutputDTO.builder()
                .id(hecho.getId())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoria(hecho.getCategoria())
                .ubicacion(hecho.getUbicacion())
                .fechaDeOcurrencia(hecho.getFechaDeOcurrencia())
                .build();
    }

    private Hecho hecho(HechoInputDTO hechoInputDTO) {
        return Hecho.builder()
                .id(hechoInputDTO.getId())
                .titulo(hechoInputDTO.getTitulo())
                .descripcion(hechoInputDTO.getDescripcion())
                .categoria(hechoInputDTO.getCategoria())
                .ubicacion(hechoInputDTO.getUbicacion())
                .fechaDeOcurrencia(hechoInputDTO.getFechaDeOcurrencia())
                .build();
    }
}