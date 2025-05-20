package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuentes.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho;
import ar.edu.utn.frba.dds.domain.repository.ICategoriaRepository;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.services.IAgregadorDeHechosService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HechosService implements IHechosService {
    private IHechosRepository hechosRepository;
    //private ICategoriaRepository categoriaRepository;
    private IAgregadorDeHechosService agregadorDeHechosService;

    public HechosService(IHechosRepository hechosRepository,
                         ICategoriaRepository categoriaRepository,
                         IAgregadorDeHechosService agregadorDeHechosService
                         ) {
        this.hechosRepository = hechosRepository;
        //this.categoriaRepository = categoriaRepository;
        this.agregadorDeHechosService = agregadorDeHechosService;
    }


    public List<Hecho> obtenerTodosLasHechos(List<Fuente> fuentes) {
        List<HechoInputDTO> hechosInput = agregadorDeHechosService.recolectarHechos(fuentes);

        List<Hecho> hechos = hechosInput.stream()
                .map(this::hechoInputDTO)
                .toList();

        hechos.forEach(hechosRepository::save);
        return hechos;
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


    //---CONVERTIDORES DE HECHOS Y DTOS---
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

    private Hecho hechoInputDTO(HechoInputDTO hechoInputDTO) {
        return Hecho.builder()
                .titulo(hechoInputDTO.getTitulo())
                .descripcion(hechoInputDTO.getDescripcion())
                .categoria(hechoInputDTO.getCategoria())
                .ubicacion(hechoInputDTO.getUbicacion())
                .fechaDeOcurrencia(hechoInputDTO.getFechaDeOcurrencia())
                .build();
    }
}