package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.FuenteDDS;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.TipoFuenteProxy;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesRepositoryJPA;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesSelector;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteService;
import lombok.Data;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Data
@Service
public class FuenteService implements IFuenteService {
    private IFuentesRepositoryJPA fuentesRepository;
    private FuenteFactory fuenteFactory;
    private HechosService hechosService;

    public FuenteService(IFuentesRepositoryJPA fuentesRepository,
                         FuenteFactory fuenteFactory,
                         HechosService hechosService) {
        this.fuentesRepository = fuentesRepository;
        this.fuenteFactory = fuenteFactory;
        this.hechosService = hechosService;
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
        FuenteDDS nuevaFuente = fuenteFactory.nuevaFuenteDDS(dto.getNombre());

        hechosService.cargarHechosFuente(nuevaFuente);

    }



    @Override
    public void eliminarFuente(String nombre) {
        fuentesRepository.deleteByNombre(nombre);
    }
}
