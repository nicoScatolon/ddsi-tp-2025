package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.DTOConverter;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.dtoAuxiliares.ColeccionFormDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        ColeccionFormDTO coleccionFormDTO = new ColeccionFormDTO(); // Usar el form DTO
        List<FuenteInputDTO> fuentes = agregadorService.getFuentesPreview();
        List<String> categorias = agregadorService.obtenerCategoriasShort();

        model.addAttribute("titulo", "Crear Colección");
        model.addAttribute("coleccionDTO", coleccionFormDTO);
        model.addAttribute("fuentes", fuentes);
        model.addAttribute("categorias", categorias);
        model.addAttribute("rol", 2);
        model.addAttribute("logeado", 1);

        return "/colecciones/create";
    }

    @PostMapping("/create")
    public String crearColeccion(@ModelAttribute ColeccionFormDTO coleccionFormDTO) {
        // Convertir el FormDTO al DTO real
        ColeccionOutputDTO coleccionDTO = DTOConverter.convertirFormToOutput(coleccionFormDTO);
        agregadorService.crearColeccion(coleccionDTO);
        return "redirect:/colecciones";
    }

    @DeleteMapping("/{handle}")
    public String eliminarColeccion(@PathVariable String handle) {
        agregadorService.eliminarColeccion(handle);
        return "redirect:/colecciones";
    }


    @GetMapping("/{handle}/editar")
    public String modificarColeccion(Model model, @PathVariable String handle) {
        ColeccionInputDTO coleccionDTO = agregadorService.obtenerColeccion(handle);
        model.addAttribute("titulo", "Modificar Colección");
        model.addAttribute("coleccionDTO", coleccionDTO);
        return "/colecciones/create";
    }
}
