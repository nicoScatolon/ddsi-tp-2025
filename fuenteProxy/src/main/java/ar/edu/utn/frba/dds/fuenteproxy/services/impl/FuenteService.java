package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.TipoFuenteProxy;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuenteService implements IFuenteService {

    private final List<IFuente> fuentes;

    public FuenteService(List<IFuente> fuentes) {
        this.fuentes = fuentes;
    }

    @Override
    public IFuente buscarPorId(Long id) {
        return fuentes.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Fuente no encontrada con ID: " + id));
    }

    @Override
    public List<IFuente> obtenerPorTipo(TipoFuenteProxy tipo) {
        return fuentes.stream()
                .filter(f -> f.getTipoFuenteProxy().equals(tipo))
                .toList();
    }

    @Override
    public List<IFuente> obtenerTodas() {
        return fuentes;
    }
}
