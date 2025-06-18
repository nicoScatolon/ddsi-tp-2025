package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.FuenteDDS;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.TipoFuenteProxy;
import reactor.core.publisher.Mono;

import java.util.List;

public class AdapterFuenteDDS implements IFuenteExternaAdapter {
    private final FuenteDDS fuenteDDS;

    public AdapterFuenteDDS(FuenteDDS fuenteDDS) {
        this.fuenteDDS = fuenteDDS;
    }

    @Override
    public Long getId() {
        return fuenteDDS.getId();
    }

    @Override
    public TipoFuenteProxy getTipo() {
        return fuenteDDS.getTipoFuenteProxy();
    }

    @Override
    public Mono<List<HechoExternoDTO>> obtenerHechos() {
        return fuenteDDS.getHechos();
    }

    @Override
    public Mono<HechoExternoDTO> obtenerPorId(Long id) {
        return fuenteDDS.getHechoPorId(id);
    }

}
