package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.estadisticas.*;
import ar.edu.utn.frba.dds.services.IEstadisticasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public List<E_HoraOcuPorCategoriaOutputDTO> obtenerEstadisticaHoraPorCategoria(@RequestBody CategoriaInputDTO catInputDTO) {
       return serviceEstadisticas.obtenerEstadisticasHoraPorCategoria(catInputDTO);
    }

    @GetMapping("/mayor_categoria")
    public List<E_MayorCategoriaOutputDTO> obtenerEstadisticasMayorCategoria() {
        return serviceEstadisticas.obtenerEstadisticasMayorCategoria();
    }

    @GetMapping("/mayor_provincia_por_categoria")
    public List<E_MayorProvPorCategoriaOutputDTO> obtenerEstadisticasMayorProvinciaPorCategoria(@RequestBody CategoriaInputDTO catInputDTO) {
        return serviceEstadisticas.obtenerEstadisticasMayorProvPorCategoria(catInputDTO);
    }

    @GetMapping("/mayor_provincia_por_coleccion")
    public List<E_MayorProvPorColeccionOutputDTO> obtenerEstadisticasMayorProvinciaPorColeccion(@RequestBody ColeccionInputDTO colInputDTO) {
        return serviceEstadisticas.obtenerEstadisticasMayorProvPorColeccion(colInputDTO);
    }

    @GetMapping("/solicitudes_de_spam")
    public List<E_SolicitudesSpamOutputDTO> obtenerEstadisticaSolicitudesDeSpam() {
        return serviceEstadisticas.obtenerEstadisticasSolicitudesSpam();
    }
}