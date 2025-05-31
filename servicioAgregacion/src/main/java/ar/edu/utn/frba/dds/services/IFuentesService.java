package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.FuenteDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;

import java.util.List;

public interface IFuentesService {
    List<Fuente> buscarFuentes();
    void agregarFuente(FuenteDTO fuenteDTO);
    void eliminarFuente(Long id);
    Fuente buscarFuentePorId(Long id);
    List<Fuente> buscarFuentePorTipo(TipoFuente tipoFuente);
    List<Fuente> buscarFuentePorTipo(List<TipoFuente> tipoFuente);
}
