package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechosFilterInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    /*
    @GetMapping("/{handle}")
    public String detallesColeccion(@ModelAttribute HechosFilterInputDTO filtros, @RequestParam(value = "page", defaultValue = "0") int paginaActual, @PathVariable String handle, Model model) {
        ColeccionPreviewInputDTO coleccion = agregadorService.obtenerColeccionPreview(handle);
        List<HechoInputDTO> hechosColeccion = agregadorService.obtenerHechosColeccion(paginaActual);

        if (filtros == null) {
            filtros = new HechosFilterInputDTO(); // para que Thymeleaf no rompa
        }

        model.addAttribute("titulo", String.format("Coleccion - %s", coleccion.getHandle()));
        model.addAttribute("coleccion", coleccion);
        model.addAttribute("hechos", hechosColeccion);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        return "/colecciones/colecciones-details";
    }
    */
}
