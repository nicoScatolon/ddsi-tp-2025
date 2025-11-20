package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.DTOConverter;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.TipoAlgoritmoConsenso;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.FiltroConsenso;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.dtoAuxiliares.ColeccionFormDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFileSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final IFileSystemService fileSystemService;

    @Value("${app.colecciones.page.size}")
    private Integer pageSize;

    @GetMapping
    public String listarColecciones(
            Model model,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) FiltroConsenso consenso) {

        List<ColeccionPreviewInputDTO> colecciones = agregadorService.obtenerColeccionesPreview(page, consenso);

        model.addAttribute("titulo", "Colecciones");
        model.addAttribute("colecciones", colecciones);
        model.addAttribute("paginaActual", page);
        model.addAttribute("filtroActual", consenso);

        return "colecciones/explore";
    }

    @GetMapping("/{handle}")
    public String detallesColeccion(@ModelAttribute HechosFilterInputDTO filtros,
                                    @RequestParam(value = "page", defaultValue = "0") int paginaActual,
                                    @RequestParam(value = "curado", defaultValue = "false") Boolean curado,
                                    @PathVariable String handle,
                                    Model model) {
        ColeccionPreviewInputDTO coleccion = agregadorService.obtenerColeccionPreviewIndividual(handle);
        List<HechoInputDTO> hechosColeccion = agregadorService.obtenerHechosColeccion(handle, paginaActual, filtros, curado);

        fileSystemService.procesarImagenPrincipalListaHechos(hechosColeccion);

        if (filtros == null) {
            filtros = new HechosFilterInputDTO(); // para que Thymeleaf no rompa
        }
        if (hechosColeccion == null) {
            hechosColeccion = new ArrayList<>();
        }

        List<String> provincias = agregadorService.obtenerProvinciasShort();
        List<String> categorias = agregadorService.obtenerCategoriasShort(); //Todo podrian ser unicamente las categorias de la coleccion, ahora manda todas
        List<String> etiquetas = agregadorService.obtenerEtiquetasShort();  //Todo podrian ser unicamente las etiquetas de la coleccion, ahora manda todas

        model.addAttribute("etiquetas", etiquetas);
        model.addAttribute("categorias", categorias);
        model.addAttribute("provincias", provincias);
        model.addAttribute("titulo", String.format("Coleccion - %s", coleccion.getHandle()));
        model.addAttribute("coleccion", coleccion);
        model.addAttribute("hechos", hechosColeccion);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("curado", curado);
        model.addAttribute("filtros", filtros);
        return "/colecciones/details";
    }

    @PreAuthorize("hasAnyRole('ADMIN','ADMINSUPERIOR')")
    @GetMapping("/create")
    public String crearColeccion(Model model) {
        String actionUrl = "/colecciones/create";
        ColeccionFormDTO coleccionFormDTO = new ColeccionFormDTO(); // Usar el form DTO
        List<FuenteInputDTO> fuentes = agregadorService.getFuentesPreview();
        List<String> categorias = agregadorService.obtenerCategoriasShort();
        List<String> provincias = agregadorService.obtenerProvinciasShort();
        List<String> etiquetas = agregadorService.obtenerEtiquetasShort();

        model.addAttribute("etiquetas", etiquetas);
        
        model.addAttribute("provincias", provincias);
        model.addAttribute("titulo", "Crear Colección");
        model.addAttribute("coleccionDTO", coleccionFormDTO);
        model.addAttribute("fuentes", fuentes);
        model.addAttribute("categorias", categorias);
        model.addAttribute("nueva", true);
        model.addAttribute("actionUrl", actionUrl);

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

    @PutMapping()
    public String editarColeccion(@ModelAttribute ColeccionFormDTO coleccionFormDTO) { //todo Forbidden
        ColeccionOutputDTO coleccionDTO = DTOConverter.convertirFormToOutput(coleccionFormDTO);
        agregadorService.editarColeccion(coleccionDTO);

        return "redirect:/colecciones";
    }

    @GetMapping("/{handle}/editar")
    public String modificarColeccion(Model model, @PathVariable String handle) {
        String actionUrl = "/colecciones";
        List<FuenteInputDTO> fuentes = agregadorService.getFuentesPreview();

        List<String> categorias = agregadorService.obtenerCategoriasShort();
        List<String> provincias = agregadorService.obtenerProvinciasShort();
        List<String> etiquetas = agregadorService.obtenerEtiquetasShort();

        ColeccionInputDTO coleccionDTO = agregadorService.obtenerColeccion(handle);

        // Convertir ColeccionInputDTO a ColeccionFormDTO
        ColeccionFormDTO formDTO = DTOConverter.convertirAFormDTO(coleccionDTO);

        List<Long> fuentesSeleccionadas = coleccionDTO.getFuentes()
                .stream()
                .map(FuenteInputDTO::getFuenteId)
                .toList();

        model.addAttribute("provincias", provincias);
        model.addAttribute("etiquetas", etiquetas);
        model.addAttribute("fuentesSeleccionadas", fuentesSeleccionadas);
        model.addAttribute("fuentes", fuentes);
        model.addAttribute("categorias", categorias);
        model.addAttribute("titulo", "Modificar Colección");
        model.addAttribute("coleccionDTO", formDTO);
        model.addAttribute("actionUrl", actionUrl);
        model.addAttribute("nueva", false);
        return "/colecciones/create";
    }

    @PutMapping("/destacar/{handle}")
    public String destacarColeccion(@PathVariable("handle") String handle){
        agregadorService.destacarColeccion(handle);

        return "redirect:/colecciones/" + handle;
    }

    @DeleteMapping("/destacar/{handle}")
    public String eliminarDestacarColeccion(@PathVariable("handle") String handle){
        agregadorService.eliminarDestacarColeccion(handle);

        return "redirect:/colecciones/" + handle;
    }
}
