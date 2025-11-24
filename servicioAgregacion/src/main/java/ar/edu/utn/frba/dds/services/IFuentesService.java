package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.FuentePreviewOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IFuentesService {
    List<Fuente> buscarFuentes();
    ResponseEntity<Void> agregarFuente(FuenteInputDTO fuenteDTO);
    ResponseEntity<String> eliminarFuente(Long id);
    void eliminarFuenteAsync(long fuenteId);
    IFuente buscarFuentePorId(Long id);
    void notificarEliminaciones (List<Hecho> hechosAEliminar);
    void actualizarHechosFuentesScheduler();

    List<FuentePreviewOutputDTO> getFuentesPreview();

}
