package ar.edu.utn.frba.dds.fuenteproxy.domain.repositories;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad.*;

import java.util.List;

public interface IFuentesRepository {
    List<IFuente> todasLasFuentes();
    List<ServidoraDeHechos> fuentesConHechos();
    List<ServidoraDeHechosConFiltros> fuentesConFiltros();
    List<ServidoraDeHechosPorId> fuentesQuePermitenBuscarPorId();
    List<ServidoraDeColecciones> fuentesConColecciones();
    List<ServidoraDeEliminaciones> fuentesQuePermitenEliminar();
}
