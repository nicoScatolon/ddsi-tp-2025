package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.FuenteDDS;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.TipoFuenteProxy;
import reactor.core.publisher.Mono;

import java.util.List;

public class AdapterFuenteDDS implements IFuenteAdapter {
    private FuenteDDS fuenteDDS;

    @Override
    public void setFuente(IFuente fuente) {
        if (! (fuente instanceof FuenteDDS) ) {throw new RuntimeException("Fuente no valida");}
        else fuenteDDS = (FuenteDDS) fuente;
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


    public Mono<HechoExternoDTO> obtenerPorId(Long id) {
        return fuenteDDS.getHechoPorId(id);
    }

}
