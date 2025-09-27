package ar.edu.utn.frba.dds.fuenteproxy.domain.repositories;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad.*;

import java.util.List;

public interface IFuentesSelector {
    List<IFuente> todasLasFuentes();
    List<ServidoraDeHechos> fuentesConHechos();
    List<ServidoraDeHechosConFiltros> fuentesConFiltros();
    List<ServidoraDeHechosPorId> fuentesQuePermitenBuscarPorId();
    List<ServidoraDeColecciones> fuentesConColecciones();
    List<ServidoraDeEliminaciones> fuentesQuePermitenEliminar();
}
