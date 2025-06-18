package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.TipoFuenteProxy;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.adapters.IFuenteAdapter;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuenteService implements IFuenteService {
    private final List<IFuenteAdapter> fuentes;

    public FuenteService(List<IFuenteAdapter> fuentes) {
        this.fuentes = fuentes;
    }

    @Override
    public IFuenteAdapter buscarPorId(Long id) {
        return fuentes.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Fuente no encontrada con ID: " + id));
    }

    @Override
    public List<IFuenteAdapter> obtenerPorTipo(TipoFuenteProxy tipo) {
        return fuentes.stream()
                .filter(f -> f.getTipo() == tipo)
                .toList();
    }

    @Override
    public List<IFuenteAdapter> obtenerTodas() {
        return fuentes;
    }

}
