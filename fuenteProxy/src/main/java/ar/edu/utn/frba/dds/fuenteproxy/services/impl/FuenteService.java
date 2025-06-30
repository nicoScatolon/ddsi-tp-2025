package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.TipoFuenteProxy;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesRepository;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuenteService implements IFuenteService {

    private final IFuentesRepository fuentesRepository;

    public FuenteService(IFuentesRepository fuentesRepository) {
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
                .filter(f -> f.getTipoFuenteProxy().equals(tipo))
                .toList();
    }

    @Override
    public List<IFuente> obtenerTodas() {
        return fuentesRepository.todasLasFuentes();
    }
}
