package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.FuenteDDS;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.TipoFuenteProxy;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesRepositoryJPA;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuentesService;
import ar.edu.utn.frba.dds.fuenteproxy.services.IHechosService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class FuentesService implements IFuentesService {
    private IFuentesRepositoryJPA fuentesRepository;
    private FuenteFactory fuenteFactory;
    private IHechosService hechosService;

    public FuentesService(IFuentesRepositoryJPA fuentesRepository,
                          FuenteFactory fuenteFactory,
                          IHechosService hechosService) {
        this.fuentesRepository = fuentesRepository;
        this.fuenteFactory = fuenteFactory;
        this.hechosService = hechosService;
    }

    @Override
    public List<FuenteOutputDTO> getFuentes(){
        return fuentesRepository.findAll()
                .stream().map(f -> DTOConverter.mapToFuenteOutputDTO(f)).toList();
    }

    @Override
    public Fuente buscarPorId(Long id) {
        return fuentesRepository.findById(id).orElse(null);
    }

    @Override
    public List<Fuente> obtenerPorTipo(TipoFuenteProxy tipo) {
        return fuentesRepository.findByTipo(tipo);
    }

    @Override
    public List<Fuente> obtenerTodas() {
        return fuentesRepository.findAll();
    }

    @Override
    public void agregarFuenteMetamapa(FuenteInputDTO dto) {
        fuenteFactory.nuevaFuenteMetaMapa(dto.getNombre(), dto.getBaseUrl());
    }

    @Override
    public void agregarFuenteDDS(FuenteInputDTO dto) {
        boolean existsFuenteByBaseUrl = fuentesRepository.existsFuenteByBaseUrl(fuenteFactory.getDdsBaseUrl());
        FuenteDDS nuevaFuente = fuenteFactory.nuevaFuenteDDS(dto.getNombre());
        if(!existsFuenteByBaseUrl) {
            hechosService.cargarHechosFuente(nuevaFuente).subscribe();
        }
    }



    @Override
    public void eliminarFuente(String nombre) {
        fuentesRepository.deleteByNombre(nombre);
    }

}
