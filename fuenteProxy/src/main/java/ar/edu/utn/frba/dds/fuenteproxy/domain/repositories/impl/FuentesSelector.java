package ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad.*;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesSelector;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesRepositoryJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FuentesSelector implements IFuentesSelector {

    private final IFuentesRepositoryJPA jpa;

    private List<Fuente> habilitadas() {
        return jpa.findByHabilitadaTrue();
    }

    private <T> List<T> porCapacidad(Class<T> capacidad) {
        return habilitadas().stream()
                .filter(capacidad::isInstance)
                .map(capacidad::cast)
                .collect(Collectors.toList());
    }

    @Override
    public List<IFuente> todasLasFuentes() {
        return this.habilitadas().stream().map(f -> (IFuente) f).toList();
    }

    @Override
    public List<ServidoraDeHechos> fuentesConHechos() {
        return this.porCapacidad(ServidoraDeHechos.class);
    }

    @Override
    public List<ServidoraDeHechosConFiltros> fuentesConFiltros() {
        return this.porCapacidad(ServidoraDeHechosConFiltros.class);
    }

    @Override
    public List<ServidoraDeHechosPorId> fuentesQuePermitenBuscarPorId() {
        return this.porCapacidad(ServidoraDeHechosPorId.class);
    }

    @Override
    public List<ServidoraDeColecciones> fuentesConColecciones() {
        return this.porCapacidad(ServidoraDeColecciones.class);
    }

    @Override
    public List<ServidoraDeEliminaciones> fuentesQuePermitenEliminar() {
        return this.porCapacidad(ServidoraDeEliminaciones.class);
    }
}
