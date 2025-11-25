package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas.PanelActividadViewDTO;
import org.springframework.http.ResponseEntity;

public interface IEstadisticasFacade {
    PanelActividadViewDTO getPanelActividad(String coleccion);
    ResponseEntity<Void> actualizarEstadisticasForzosamente();
    ResponseEntity<Void> eliminarEstadisticasViejas();
}
