package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.AlgoritmoConcensoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/colecciones")
@RequiredArgsConstructor
public class ColeccionesController {
    private final IAgregadorService agregadorService;

    @Value("${app.colecciones.page.size}")
    private Integer pageSize;

    @GetMapping
    public String listarColecciones(@RequestParam(value = "page", defaultValue = "0") int paginaActual, Model model) {
        List<ColeccionPreviewInputDTO> colecciones = agregadorService.obtenerColeccionesPreview(paginaActual);
        model.addAttribute("titulo", String.format("Colecciones - Pagina %d", paginaActual+1));
        model.addAttribute("colecciones", colecciones);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        return "/colecciones/explore";
    }

    @GetMapping("/{handle}")
    public String detallesColeccion(@ModelAttribute HechosFilterInputDTO filtros,
                                    @RequestParam(value = "page", defaultValue = "0") int paginaActual,
                                    @RequestParam(value = "curado", defaultValue = "false") Boolean curado,
                                    @PathVariable String handle,
                                    Model model) {
        ColeccionPreviewInputDTO coleccion = agregadorService.obtenerColeccionPreviewIndividual(handle);
        List<HechoInputDTO> hechosColeccion = agregadorService.obtenerHechosColeccion(handle, paginaActual, filtros, curado);

        if (filtros == null) {
            filtros = new HechosFilterInputDTO(); // para que Thymeleaf no rompa
        }
        if (hechosColeccion == null) {
            hechosColeccion = new ArrayList<>();
        }

        model.addAttribute("titulo", String.format("Coleccion - %s", coleccion.getHandle()));
        model.addAttribute("coleccion", coleccion);
        model.addAttribute("hechos", hechosColeccion);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("curado", curado);
        model.addAttribute("filtros", filtros);
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        return "/colecciones/colecciones-details";
    }

    @GetMapping("/create")
    public String crearColeccion(Model model) {
        ColeccionOutputDTO coleccionDTO = instanciarColeccionOutput();
        List<FuenteInputDTO> fuentes = agregadorService.getFuentesPreview();

        model.addAttribute("titulo", "Crear Coleccion");
        model.addAttribute("coleccionDTO", coleccionDTO);
        model.addAttribute("fuentes", fuentes);
        model.addAttribute("rol", 2);
        model.addAttribute("logeado", 1);

        return "/colecciones/create";
    }

    @PostMapping("/create") public String crearColeccion(
            @ModelAttribute ColeccionOutputDTO coleccionDTO) {

        return "redirect:/colecciones";
    }

    private ColeccionOutputDTO instanciarColeccionOutput() {
        return ColeccionOutputDTO.builder()
                .listaCriterios(new HashSet<>())
                .listaIdsFuentes(new HashSet<>())
                .algoritmoConsenso(new AlgoritmoConcensoOutputDTO())
                .build();
    }
}
