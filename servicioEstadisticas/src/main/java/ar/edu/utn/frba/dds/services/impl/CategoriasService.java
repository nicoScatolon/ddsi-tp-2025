package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOconverter;
import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import ar.edu.utn.frba.dds.services.ICategoriasService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoriasService implements ICategoriasService {
    private final ICategoriasRepository categoriasRepository;

    private final WebClient webClient;

    @Value("${agregador.base-url}") private String urlAgregador;

    public CategoriasService(ICategoriasRepository categoriasRepository, WebClient.Builder webClientBuilder,
                             @Value("${agregador.base-url}") String urlAgregador) {
        this.categoriasRepository = categoriasRepository;
        this.urlAgregador = urlAgregador;
        this.webClient = webClientBuilder.baseUrl(urlAgregador).build();
    }

    public Categoria findById(String idCategoria) {
        Categoria categoria = categoriasRepository.findById(idCategoria).orElse(null);
        return categoria;
    }

    @Transactional
    public void actualizarCategorias(){
        List<CategoriaInputDTO> categoriasDTO = webClient.get()
                .uri("/api/privada/categorias")
                .retrieve()
                .bodyToFlux(CategoriaInputDTO.class)
                .collectList()
                .block();

        if (categoriasDTO == null) return; //no consegui categorias

        List<Categoria> categoriasExistentes = categoriasRepository.findAll();

        // Busco por cambios y nuevas categorias
        for (CategoriaInputDTO categoriaDTO : categoriasDTO) {
            var categoriaExistente = categoriasExistentes.stream()
                    .filter(c -> c.getCodigoCategoria().equals(categoriaDTO.getCodigoCategoria()))
                    .findFirst();

            categoriaExistente.ifPresentOrElse(categoria -> {
                //si existe pero no matchean los datos la actualizo
                if (!categoria.getNombre().equals(categoriaDTO.getNombre())) {
                    categoria.setNombre(categoriaDTO.getNombre());
                    categoria.setFechaActualizacion(LocalDateTime.now());
                    this.categoriasRepository.save(categoria);
                }
            }
            , () -> {
                //si no existe la creo
                        Categoria nuevaCategoria = DTOconverter.categoriaInputDTO(categoriaDTO);
                        nuevaCategoria.setFechaActualizacion(LocalDateTime.now());
                        this.categoriasRepository.save(nuevaCategoria);
                    }
            );
        }

        //ahora busco las que fueron eliminadas -> no me llegaron cuando las pedi
        List<String> idsDTOs = categoriasDTO.stream().map(CategoriaInputDTO::getCodigoCategoria).toList();

        categoriasExistentes.stream()
                .filter( c -> ! idsDTOs.contains(c.getCodigoCategoria()) )
                .forEach(categoriasRepository::delete);
    }

    // test

    @Override
    public void actualizarCategoriasTest(List<Categoria> categorias) {
        if (categorias == null) return; //no consegui categorias

        List<Categoria> categoriasExistentes = categoriasRepository.findAll();

        // Busco por cambios y nuevas categorias
        for (Categoria categoriaInput : categorias) {
            var categoriaExistente = categoriasExistentes.stream()
                    .filter(c -> c.getCodigoCategoria().equals(categoriaInput.getCodigoCategoria()))
                    .findFirst();

            categoriaExistente.ifPresentOrElse(categoria -> {
                        //si existe pero no matchean los datos la actualizo
                        if (!categoria.getNombre().equals(categoriaInput.getNombre())) {
                            categoria.setNombre(categoriaInput.getNombre());
                            categoria.setFechaActualizacion(LocalDateTime.now());
                            this.categoriasRepository.save(categoria);
                        }
                    }
                    , () -> {
                        //si no existe la creo
                        Categoria nuevaCategoria = categoriaInput;
                        nuevaCategoria.setFechaActualizacion(LocalDateTime.now());
                        this.categoriasRepository.save(nuevaCategoria);
                    }
            );
        }

        //ahora busco las que fueron eliminadas -> no me llegaron cuando las pedi
        List<String> idsDTOs = categorias.stream().map(Categoria::getCodigoCategoria).toList();

        categoriasExistentes.stream()
                .filter( c -> ! idsDTOs.contains(c.getCodigoCategoria()) )
                .forEach(categoriasRepository::delete);
    }
}
