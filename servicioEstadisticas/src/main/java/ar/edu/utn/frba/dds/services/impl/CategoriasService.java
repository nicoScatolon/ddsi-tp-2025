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

    @Value("agregador.base-url")
    private String urlAgregador;

    public CategoriasService(ICategoriasRepository categoriasRepository) {
        this.categoriasRepository = categoriasRepository;
        this.webClient = WebClient.builder().baseUrl(urlAgregador).build();
    }

    public Categoria findById(CategoriaInputDTO categoriaDTO) {
        Categoria categoria = categoriasRepository.findById(categoriaDTO.getId()).orElse(null);
        if (categoria == null) {return null;}
        if (!Objects.equals(categoriaDTO.getNombre(), categoria.getNombre())) {return null;}
        return categoria;
    }

    @Transactional
    public void actualizarCategorias(){
        //TODO hacer controller de categorias en servicio de Agregacion
        List<CategoriaInputDTO> categoriasDTO = webClient.get()
                .uri("/api/categorias")
                .retrieve()
                .bodyToFlux(CategoriaInputDTO.class)
                .collectList()
                .block();

        if (categoriasDTO == null) return; //no consegui categorias

        List<Categoria> categoriasExistentes = categoriasRepository.findAll();

        // Busco por cambios y nuevas categorias
        for (CategoriaInputDTO categoriaDTO : categoriasDTO) {
            var categoriaExistente = categoriasExistentes.stream()
                    .filter(c -> c.getId().equals(categoriaDTO.getId()))
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
                        this.categoriasRepository.save(nuevaCategoria);
                    }
            );
        }

        //ahora busco las que fueron eliminadas -> no me llegaron cuando las pedi
        List<String> idsDTOs = categoriasDTO.stream().map(CategoriaInputDTO::getId).toList();

        categoriasExistentes.stream()
                .filter( c -> ! idsDTOs.contains(c.getId()) )
                .forEach(categoriasRepository::delete);
    }
}
