package ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad.*;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FuentesRepository implements IFuentesRepository {
    private final List<IFuente> todasLasFuentes;
    private final List<ServidoraDeHechos> servidorasDeHechos;
    private final List<ServidoraDeHechosConFiltros> servidorasConFiltros;
    private final List<ServidoraDeHechosPorId> servidorasPorId;
    private final List<ServidoraDeColecciones> servidorasDeColecciones;
    private final List<ServidoraDeEliminaciones> servidorasDeEliminaciones;

    public FuentesRepository(
            List<IFuente> todasLasFuentes,
            List<ServidoraDeHechos> servidorasDeHechos,
            List<ServidoraDeHechosConFiltros> servidorasConFiltros,
            List<ServidoraDeHechosPorId> servidorasPorId,
            List<ServidoraDeColecciones> servidorasDeColecciones,
            List<ServidoraDeEliminaciones> servidorasDeEliminaciones
    ) {
        this.todasLasFuentes = todasLasFuentes;
        this.servidorasDeHechos = servidorasDeHechos;
        this.servidorasConFiltros = servidorasConFiltros;
        this.servidorasPorId = servidorasPorId;
        this.servidorasDeColecciones = servidorasDeColecciones;
        this.servidorasDeEliminaciones = servidorasDeEliminaciones;
    }

    @Override
    public List<IFuente> todasLasFuentes() {
        return todasLasFuentes;
    }

    @Override
    public List<ServidoraDeHechos> fuentesConHechos() {return servidorasDeHechos;}

    @Override
    public List<ServidoraDeHechosConFiltros> fuentesConFiltros() {return servidorasConFiltros;}

    @Override
    public List<ServidoraDeHechosPorId> fuentesQuePermitenBuscarPorId() {
        return servidorasPorId;
    }

    @Override
    public List<ServidoraDeColecciones> fuentesConColecciones() {
        return servidorasDeColecciones;
    }

    @Override
    public List<ServidoraDeEliminaciones> fuentesQuePermitenEliminar() {
        return servidorasDeEliminaciones;
    }
}
