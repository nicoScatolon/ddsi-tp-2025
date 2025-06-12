package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.FuenteMetaMapa;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.TipoFuenteProxy;
import reactor.core.publisher.Mono;

import java.util.List;

public class AdapterFuenteMetaMapa implements IFuenteMetaMapaAdapter {
    private final FuenteMetaMapa fuenteMetaMapa;

    public AdapterFuenteMetaMapa(FuenteMetaMapa fuenteMetaMapa) {
        this.fuenteMetaMapa = fuenteMetaMapa;
    }

    @Override
    public Long getId() {
        return fuenteMetaMapa.getId();
    }

    @Override
    public TipoFuenteProxy getTipo() {
        return fuenteMetaMapa.getTipoFuenteProxy();
    }

    @Override
    public Mono<List<HechoExternoDTO>> obtenerHechos() {
        return fuenteMetaMapa.getHechos();
    }

    @Override
    public Mono<List<HechoExternoDTO>> buscarConFiltros(String categoria, String fechaReporteDesde, String fechaReporteHasta,  String fechaAcontecimientoDesde,  String fechaAcontecimientoHasta,  String ubicacion) {
        return fuenteMetaMapa.buscarConFiltros(categoria, fechaReporteDesde, fechaReporteHasta,fechaAcontecimientoDesde, fechaAcontecimientoHasta, ubicacion);
    }

    @Override
    public Mono<List<ColeccionInputDTO>> buscarTodasLasColecciones() {
        return fuenteMetaMapa.buscarTodasLasColecciones();
    }

    @Override
    public Mono<List<HechoExternoDTO>> buscarPorColeccion(String identificador) {
        return fuenteMetaMapa.buscarPorColeccion(identificador);
    }

    @Override
    public Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoOutputDTO solicitud) {
        return fuenteMetaMapa.crearSolicitudEliminacion(solicitud);
    }
}
