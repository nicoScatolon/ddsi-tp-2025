package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.util.List;

public interface IFuentesService {
    List<IFuente> buscarFuentes();
    Boolean agregarFuente(FuenteInputDTO fuenteDTO);
    void eliminarFuente(Long id);
    IFuente buscarFuentePorId(Long id);
    List<IFuente> buscarFuentePorTipo(TipoFuente tipoFuente);
    List<IFuente> buscarFuentePorTipo(List<TipoFuente> tipoFuente);
    void notificarEliminaciones (List<Hecho> hechosAEliminar);
    void actualizarHechosFuentesScheduler();
}
