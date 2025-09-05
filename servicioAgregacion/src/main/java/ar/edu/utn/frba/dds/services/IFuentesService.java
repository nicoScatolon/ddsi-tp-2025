package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IFuentesService {
    List<Fuente> buscarFuentes();
    ResponseEntity<Void> agregarFuente(FuenteInputDTO fuenteDTO);
    ResponseEntity<Void> eliminarFuente(Long id);
    IFuente buscarFuentePorId(Long id);
    List<Fuente> buscarFuentePorTipo(TipoFuente tipoFuente);
    List<Fuente> buscarFuentePorTipo(List<TipoFuente> tipoFuente);
    void notificarEliminaciones (List<Hecho> hechosAEliminar);
    void actualizarHechosFuentesScheduler();

}
