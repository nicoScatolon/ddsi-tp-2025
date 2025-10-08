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
    public List<E_HoraOcuPorCategoriaOutputDTO> obtenerEstadisticaHoraPorCategoria( @RequestParam(required = false) String idCategoria) {
       return serviceEstadisticas.obtenerEstadisticasHoraPorCategoria(idCategoria);
    }

    @GetMapping("/hora_por_categoria/CSV")
    public String obtenerEstadisticaHoraPorCategoriaCSV(@RequestParam(required = false) String idCategoria) {
        List<E_HoraOcuPorCategoriaOutputDTO> listaDTO =  serviceEstadisticas.obtenerEstadisticasHoraPorCategoria(idCategoria);
        return ExportadorCSV.exportHoraOcurrenciaPorCategoria(listaDTO);
    }

    @GetMapping("/mayor_categoria")
    public List<E_MayorCategoriaOutputDTO> obtenerEstadisticasMayorCategoria() {
        return serviceEstadisticas.obtenerEstadisticasMayorCategoria();
    }

    @GetMapping("/mayor_categoria/CSV")
    public String obtenerEstadisticasMayorCategoriaCSV() {
        List<E_MayorCategoriaOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasMayorCategoria();
        return ExportadorCSV.exportMayorCategoria(listaDTO);
    }

    @GetMapping("/mayor_provincia_por_categoria")
    public List<E_MayorProvPorCategoriaOutputDTO> obtenerEstadisticasMayorProvinciaPorCategoria(@RequestParam(required = false) String idCategoria) {
        return serviceEstadisticas.obtenerEstadisticasMayorProvPorCategoria(idCategoria);
    }

    @GetMapping("/mayor_provincia_por_categoria/CSV")
    public String obtenerEstadisticasMayorProvinciaPorCategoriaCSV(@RequestParam(required = false) String idCategoria) {
        List<E_MayorProvPorCategoriaOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasMayorProvPorCategoria(idCategoria);
        return ExportadorCSV.exportMayorProvinciaPorCategoria(listaDTO);
    }

    @GetMapping("/mayor_provincia_por_coleccion")
    public List<E_MayorProvPorColeccionOutputDTO> obtenerEstadisticasMayorProvinciaPorColeccion(@RequestParam(required = false) String handleColeccion) {
        return serviceEstadisticas.obtenerEstadisticasMayorProvPorColeccion(handleColeccion);
    }

    @GetMapping("/mayor_provincia_por_coleccion/CSV")
    public String obtenerEstadisticasMayorProvinciaPorColeccionCSV(@RequestParam(required = false) String handleColeccion) {
        List<E_MayorProvPorColeccionOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasMayorProvPorColeccion(handleColeccion);
        return ExportadorCSV.exportMayorProvinciaPorColeccion(listaDTO);
    }

    @GetMapping("/solicitudes_de_spam")
    public List<E_SolicitudesSpamOutputDTO> obtenerEstadisticaSolicitudesDeSpam() {
        return serviceEstadisticas.obtenerEstadisticasSolicitudesSpam();
    }

    @GetMapping("/solicitudes_de_spam/CSV")
    public String obtenerEstadisticaSolicitudesDeSpamCSV() {
        List<E_SolicitudesSpamOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasSolicitudesSpam();
        return ExportadorCSV.exportSolicitudesSpam(listaDTO);
    }

    // test //

    @PostMapping("/test")
    public void test(@RequestBody List<HechoInputDTO> hechosInputDTO) {
        serviceEstadisticas.generarEstadisticasTest(hechosInputDTO);
    }

    private List<E_HoraOcuPorCategoriaOutputDTO> getMockHoraOcuPorCategoria() {
        List<E_HoraOcuPorCategoriaOutputDTO> mockList = List.of(
                E_HoraOcuPorCategoriaOutputDTO.builder()
                        .id(1L)
                        .categoriaDTO( CategoriaOutputDTO.builder().id("id1").nombre("Atraco").build() )
                        .horaDia(22)
                        .cantHechosHora(15)
                        .cantHechosTotales(100)
                        .fechaDeCalculo(LocalDateTime.now().minusDays(1))
                        .build(),
                E_HoraOcuPorCategoriaOutputDTO.builder()
                        .id(2L)
                        .categoriaDTO( CategoriaOutputDTO.builder().id("id2").nombre("Robo").build() )
                        .horaDia(18)
                        .cantHechosHora(10)
                        .cantHechosTotales(80)
                        .fechaDeCalculo(LocalDateTime.now().minusDays(2))
                        .build(),
                E_HoraOcuPorCategoriaOutputDTO.builder()
                        .id(3L)
                        .categoriaDTO( CategoriaOutputDTO.builder().id("id3").nombre("Vandalismo").build() )
                        .horaDia(2)
                        .cantHechosHora(5)
                        .cantHechosTotales(40)
                        .fechaDeCalculo(LocalDateTime.now().minusDays(3))
                        .build()
        );
        return mockList;
    }
}