package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas.PanelActividadViewDTO;

public interface IEstadisticasFacade {
    PanelActividadViewDTO getPanelActividad(String coleccion);
}
