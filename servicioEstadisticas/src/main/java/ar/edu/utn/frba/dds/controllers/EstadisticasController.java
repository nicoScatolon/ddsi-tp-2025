package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.estadisticas.*;
import ar.edu.utn.frba.dds.domain.entities.ExportadorCSV;
import ar.edu.utn.frba.dds.services.IEstadisticasService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasController {
    private final IEstadisticasService serviceEstadisticas;

    public EstadisticasController(IEstadisticasService serviceEstadisticas) {
        this.serviceEstadisticas = serviceEstadisticas;
    }

    @GetMapping("/hora_por_categoria")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public List<E_HoraOcuPorCategoriaOutputDTO> obtenerEstadisticaHoraPorCategoria( @RequestParam(required = false) String idCategoria) {
        return serviceEstadisticas.obtenerEstadisticasHoraPorCategoria(idCategoria);
    }

    @GetMapping("/hora_por_categoria/CSV")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public String obtenerEstadisticaHoraPorCategoriaCSV(@RequestParam(required = false) String idCategoria) {
        List<E_HoraOcuPorCategoriaOutputDTO> listaDTO =  serviceEstadisticas.obtenerEstadisticasHoraPorCategoria(idCategoria);
        return ExportadorCSV.exportHoraOcurrenciaPorCategoria(listaDTO);
    }

    @GetMapping("/mayor_categoria")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public List<E_MayorCategoriaOutputDTO> obtenerEstadisticasMayorCategoria() {
        return serviceEstadisticas.obtenerEstadisticasMayorCategoria();
    }

    @GetMapping("/mayor_categoria/CSV")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public String obtenerEstadisticasMayorCategoriaCSV() {
        List<E_MayorCategoriaOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasMayorCategoria();
        return ExportadorCSV.exportMayorCategoria(listaDTO);
    }

    @GetMapping("/mayor_provincia_por_categoria")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public List<E_MayorProvPorCategoriaOutputDTO> obtenerEstadisticasMayorProvinciaPorCategoria(@RequestParam(required = false) String idCategoria) {
        return serviceEstadisticas.obtenerEstadisticasMayorProvPorCategoria(idCategoria);
    }

    @GetMapping("/mayor_provincia_por_categoria/CSV")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public String obtenerEstadisticasMayorProvinciaPorCategoriaCSV(@RequestParam(required = false) String idCategoria) {
        List<E_MayorProvPorCategoriaOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasMayorProvPorCategoria(idCategoria);
        return ExportadorCSV.exportMayorProvinciaPorCategoria(listaDTO);
    }

    @GetMapping("/mayor_provincia_por_coleccion")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public List<E_MayorProvPorColeccionOutputDTO> obtenerEstadisticasMayorProvinciaPorColeccion(@RequestParam(required = false) String handleColeccion) {
        return serviceEstadisticas.obtenerEstadisticasMayorProvPorColeccion(handleColeccion);
    }

    @GetMapping("/mayor_provincia_por_coleccion/CSV")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public String obtenerEstadisticasMayorProvinciaPorColeccionCSV(@RequestParam(required = false) String handleColeccion) {
        List<E_MayorProvPorColeccionOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasMayorProvPorColeccion(handleColeccion);
        return ExportadorCSV.exportMayorProvinciaPorColeccion(listaDTO);
    }

    @GetMapping("/solicitudes_de_spam")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public List<E_SolicitudesSpamOutputDTO> obtenerEstadisticaSolicitudesDeSpam() {
        return serviceEstadisticas.obtenerEstadisticasSolicitudesSpam();
    }

    @GetMapping("/solicitudes_de_spam/CSV")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public String obtenerEstadisticaSolicitudesDeSpamCSV() {
        List<E_SolicitudesSpamOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasSolicitudesSpam();
        return ExportadorCSV.exportSolicitudesSpam(listaDTO);
    }

    // test //

    /*
    @PostMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public void test(@RequestBody List<HechoInputDTO> hechosInputDTO) {
        serviceEstadisticas.generarEstadisticasTest(hechosInputDTO);
    }
    */

}
