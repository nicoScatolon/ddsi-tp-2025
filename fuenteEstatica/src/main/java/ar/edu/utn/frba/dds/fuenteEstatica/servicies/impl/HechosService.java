package ar.edu.utn.frba.dds.fuenteEstatica.servicies.impl;


import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.ImportadorHechos;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.servicies.ICategoriasService;
import ar.edu.utn.frba.dds.fuenteEstatica.servicies.IHechosService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;
    private final ICategoriasService categoriasService;

    public HechosService(IHechosRepository hechosRepository, ICategoriasService categoriasService) {
        this.hechosRepository = hechosRepository;
        this.categoriasService = categoriasService;
    }

    @Override
    public List<Hecho> importarArchivoHechos(String path, ImportadorHechos importador){
        //TODO verificar que el ususario sea adminstrador - ver como llegaria el dato del usuario, si por un DTO o como
        //TODO preguntar como viene el path, si pelado como un string o con un DTO
        //TODO preguntar si es necesario realizar la verificacion del tipo de archivo cargado o si viene de afuera
        //  implementacion futura posible: recibir por query params el tipo de archivo o intentar leerlo y determinarlo aca en el service
        List<Hecho> hechos = new ArrayList<Hecho>();
        hechos = importador.importarHechosArchivo(path);
        hechos.forEach(hechosRepository::save);
        return hechos;
    }

    @Override
    public List<HechoOutputDTO> getAllHechosParaActualizar(){
        List<Hecho> hechos = hechosRepository.findAll().stream()
                .filter(Hecho::getActualizar)
                .toList();
        hechos.forEach(h -> h.setActualizar(false));
        return hechos.stream().map(this::hechoToDTO).toList();
    }

    private HechoOutputDTO hechoToDTO(Hecho hecho){
        var dto = new HechoOutputDTO();
        dto.setId(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setUbicacion(hecho.getUbicacion());
        dto.setCategoria(categoriatoDTO( hecho.getCategoria() ) );
        dto.setFechaDeCarga(hecho.getFechaDeCarga());
        dto.setFechaDeOcurrencia(hecho.getFechaDeOcurrencia());
        dto.setId(hecho.getId());
        return dto;
    }

    private CategoriaOutputDTO categoriatoDTO(String nombreCategoria){
        return CategoriaOutputDTO.builder()
                .id(categoriasService.obtenerIdCategoria(nombreCategoria))
                .nombre(nombreCategoria)
                .build();
    }
}
