package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.output.estadisticas.*;
import ar.edu.utn.frba.dds.domain.entities.ExportadorCSV;
import ar.edu.utn.frba.dds.services.IEstadisticasService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public List<E_HoraOcuPorCategoriaOutputDTO> obtenerEstadisticaHoraPorCategoria(
            @RequestParam(required = false) String idCategoria,
            @RequestParam(required = false, defaultValue = "false") Boolean mostrarAntiguas ) {
        return serviceEstadisticas.obtenerEstadisticasHoraPorCategoria(idCategoria, mostrarAntiguas);
    }

    @GetMapping("/mayor_categoria")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public List<E_MayorCategoriaOutputDTO> obtenerEstadisticasMayorCategoria(
            @RequestParam(required = false, defaultValue = "false") Boolean mostrarAntiguas ) {
        return serviceEstadisticas.obtenerEstadisticasMayorCategoria(mostrarAntiguas);
    }

    @GetMapping("/mayor_provincia_por_categoria")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public List<E_MayorProvPorCategoriaOutputDTO> obtenerEstadisticasMayorProvinciaPorCategoria(
            @RequestParam(required = false) String idCategoria,
            @RequestParam(required = false, defaultValue = "false") Boolean mostrarAntiguas ) {
        return serviceEstadisticas.obtenerEstadisticasMayorProvPorCategoria(idCategoria, mostrarAntiguas);
    }

    @GetMapping("/mayor_provincia_por_coleccion")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public List<E_MayorProvPorColeccionOutputDTO> obtenerEstadisticasMayorProvinciaPorColeccion(
            @RequestParam(required = false) String handleColeccion,
            @RequestParam(required = false, defaultValue = "false") Boolean mostrarAntiguas ) {
        return serviceEstadisticas.obtenerEstadisticasMayorProvPorColeccion(handleColeccion, mostrarAntiguas);
    }

    @GetMapping("/solicitudes_de_spam")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public List<E_SolicitudesSpamOutputDTO> obtenerEstadisticaSolicitudesDeSpam(
            @RequestParam(required = false, defaultValue = "false") Boolean mostrarAntiguas ) {
        return serviceEstadisticas.obtenerEstadisticasSolicitudesSpam(mostrarAntiguas);
    }

    // --- CSV --- //

    @GetMapping("/hora_por_categoria/CSV")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public String obtenerEstadisticaHoraPorCategoriaCSV(@RequestParam(required = false) String idCategoria) {
        List<E_HoraOcuPorCategoriaOutputDTO> listaDTO =  serviceEstadisticas.obtenerEstadisticasHoraPorCategoria(idCategoria, true);
        return ExportadorCSV.exportHoraOcurrenciaPorCategoria(listaDTO);
    }

    @GetMapping("/mayor_categoria/CSV")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public String obtenerEstadisticasMayorCategoriaCSV() {
        List<E_MayorCategoriaOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasMayorCategoria(true);
        return ExportadorCSV.exportMayorCategoria(listaDTO);
    }

    @GetMapping("/mayor_provincia_por_categoria/CSV")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public String obtenerEstadisticasMayorProvinciaPorCategoriaCSV(@RequestParam(required = false) String idCategoria) {
        List<E_MayorProvPorCategoriaOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasMayorProvPorCategoria(idCategoria, true);
        return ExportadorCSV.exportMayorProvinciaPorCategoria(listaDTO);
    }

    @GetMapping("/mayor_provincia_por_coleccion/CSV")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public String obtenerEstadisticasMayorProvinciaPorColeccionCSV(@RequestParam(required = false) String handleColeccion) {
        List<E_MayorProvPorColeccionOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasMayorProvPorColeccion(handleColeccion, true);
        return ExportadorCSV.exportMayorProvinciaPorColeccion(listaDTO);
    }

    @GetMapping("/solicitudes_de_spam/CSV")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public String obtenerEstadisticaSolicitudesDeSpamCSV() {
        List<E_SolicitudesSpamOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasSolicitudesSpam(true);
        return ExportadorCSV.exportSolicitudesSpam(listaDTO);
    }


    // test //

    @PostMapping("/test")
    @PreAuthorize("permitAll()")
    public void generarEstadisticasTest(){
        serviceEstadisticas.generarEstadisticas();
    }

    /*
    @PostMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public void test(@RequestBody List<HechoInputDTO> hechosInputDTO) {
        serviceEstadisticas.generarEstadisticasTest(hechosInputDTO);
    }
    */

}
