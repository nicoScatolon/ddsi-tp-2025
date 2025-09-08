package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.FuenteDDS;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.FuenteMetaMapa;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.TipoFuenteProxy;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesRepositoryJPA;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesSelector;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class FuenteService implements IFuenteService {
    private IFuentesRepositoryJPA fuenteRepository;
    private final IFuentesSelector fuentesRepository;
    private FuenteFactory fuenteFactory;

    public FuenteService(IFuentesSelector fuentesRepository) {
        this.fuentesRepository = fuentesRepository;
    }

    @Override
    public IFuente buscarPorId(Long id) {
        return fuentesRepository.todasLasFuentes().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Fuente no encontrada con ID: " + id));
    }

    @Override
    public List<IFuente> obtenerPorTipo(TipoFuenteProxy tipo) {
        return fuentesRepository.todasLasFuentes().stream()
                .filter(f -> f.getTipo().equals(tipo))
                .toList();
    }

    @Override
    public List<IFuente> obtenerTodas() {
        return fuentesRepository.todasLasFuentes();
    }

    @Override
    public void agregarFuenteDDS(String nombre) {
        FuenteDDS fuenteDDS = fuenteFactory.nuevaFuenteDDS(nombre);
        fuenteRepository.save(fuenteDDS);
    }

    @Override
    public void agregarFuenteMetamapa(String nombre) {
        FuenteMetaMapa fuenteMetaMapa = fuenteFactory.nuevaFuenteMetaMapa(nombre);
        fuenteRepository.save(fuenteMetaMapa);
    }

    @Override
    public void eliminarFuente(String nombre) {
        fuenteRepository.deleteByNombre(nombre);
    }
}
