package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.estadisticas.*;
import ar.edu.utn.frba.dds.domain.entities.ExportadorCSV;
import ar.edu.utn.frba.dds.services.IEstadisticasService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EstadisticasController {
    private final IEstadisticasService serviceEstadisticas;

    public EstadisticasController(IEstadisticasService serviceEstadisticas) {
        this.serviceEstadisticas = serviceEstadisticas;
    }

    @GetMapping("/test")
    public void test() {
        serviceEstadisticas.generarEstadisticasTest();
    }

    @GetMapping("/hora_por_categoria")
    public List<E_HoraOcuPorCategoriaOutputDTO> obtenerEstadisticaHoraPorCategoria(@RequestBody CategoriaInputDTO catInputDTO) {
       return serviceEstadisticas.obtenerEstadisticasHoraPorCategoria(catInputDTO);
    }

    @GetMapping("/hora_por_categoria/CSV")
    public String obtenerEstadisticaHoraPorCategoriaCSV(@RequestBody CategoriaInputDTO catInputDTO) {
        List<E_HoraOcuPorCategoriaOutputDTO> listaDTO =  serviceEstadisticas.obtenerEstadisticasHoraPorCategoria(catInputDTO);
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
    public List<E_MayorProvPorCategoriaOutputDTO> obtenerEstadisticasMayorProvinciaPorCategoria(@RequestBody CategoriaInputDTO catInputDTO) {
        return serviceEstadisticas.obtenerEstadisticasMayorProvPorCategoria(catInputDTO);
    }

    @GetMapping("/mayor_provincia_por_categoria/CSV")
    public String obtenerEstadisticasMayorProvinciaPorCategoriaCSV(@RequestBody CategoriaInputDTO catInputDTO) {
        List<E_MayorProvPorCategoriaOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasMayorProvPorCategoria(catInputDTO);
        return ExportadorCSV.exportMayorProvinciaPorCategoria(listaDTO);
    }

    @GetMapping("/mayor_provincia_por_coleccion")
    public List<E_MayorProvPorColeccionOutputDTO> obtenerEstadisticasMayorProvinciaPorColeccion(@RequestBody ColeccionInputDTO colInputDTO) {
        return serviceEstadisticas.obtenerEstadisticasMayorProvPorColeccion(colInputDTO);
    }

    @GetMapping("/mayor_provincia_por_coleccion/CSV")
    public String obtenerEstadisticasMayorProvinciaPorColeccionCSV(@RequestBody ColeccionInputDTO colInputDTO) {
        List<E_MayorProvPorColeccionOutputDTO> listaDTO = serviceEstadisticas.obtenerEstadisticasMayorProvPorColeccion(colInputDTO);
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