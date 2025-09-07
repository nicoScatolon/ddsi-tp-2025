package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.entities.Estadisticas.E_HoraOcurrenciaPorCategoria;
import ar.edu.utn.frba.dds.services.IEstadisticasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EstadisticasController {
    private final IEstadisticasService serviceEstadisticas;

    public EstadisticasController(IEstadisticasService serviceEstadisticas) {
        this.serviceEstadisticas = serviceEstadisticas;
    }

    @GetMapping("/hora_por_categoria")
    public List<E_HoraOcurrenciaPorCategoria> obtenerEstadisticaHoraPorCategoria() {
        return null;
    }
}
