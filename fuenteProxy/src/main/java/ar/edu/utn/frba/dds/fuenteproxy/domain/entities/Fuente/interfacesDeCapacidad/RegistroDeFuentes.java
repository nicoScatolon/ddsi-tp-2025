package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RegistroDeFuentes {
    private final List<ServidoraDeHechos> servidorasDeHechos;
    private final List<ServidoraDeHechosConFiltros> servidorasConFiltros;
    private final List<ServidoraDeHechosPorId> servidorasPorId;
    private final List<ServidoraDeColecciones> servidorasDeColecciones;
    private final List<ServidoraDeEliminaciones> servidorasDeEliminaciones;

    public RegistroDeFuentes(
            List<ServidoraDeHechos> servidorasDeHechos,
            List<ServidoraDeHechosConFiltros> servidorasConFiltros,
            List<ServidoraDeHechosPorId> servidorasPorId,
            List<ServidoraDeColecciones> servidorasDeColecciones,
            List<ServidoraDeEliminaciones> servidorasDeEliminaciones
    ) {
        this.servidorasDeHechos = servidorasDeHechos;
        this.servidorasConFiltros = servidorasConFiltros;
        this.servidorasPorId = servidorasPorId;
        this.servidorasDeColecciones = servidorasDeColecciones;
        this.servidorasDeEliminaciones = servidorasDeEliminaciones;
    }



    public List<ServidoraDeHechos> todasLasFuentes() {
        return servidorasDeHechos;
    }

    public List<ServidoraDeHechosConFiltros> fuentesConFiltros() {
        return servidorasConFiltros;
    }

    public List<ServidoraDeHechosPorId> fuentesQuePermitenBuscarPorId() {
        return servidorasPorId;
    }

    public List<ServidoraDeColecciones> fuentesConColecciones() {
        return servidorasDeColecciones;
    }

    public List<ServidoraDeEliminaciones> fuentesQuePermitenEliminar() {
        return servidorasDeEliminaciones;
    }
}
