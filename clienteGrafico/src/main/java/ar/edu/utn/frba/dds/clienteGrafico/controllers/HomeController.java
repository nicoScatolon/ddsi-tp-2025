package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFileSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final IAgregadorService agregadorService;
    private final IFileSystemService fileSystemService;
    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<ColeccionPreviewInputDTO> listaColeccionesDestacadasDTO = agregadorService.obtenerColeccionesDestacadas();
        List<HechoInputDTO> listaHechosDestacadosDTO = agregadorService.obtenerHechosDestacados();

        fileSystemService.procesarImagenPrincipalListaHechos(listaHechosDestacadosDTO);
        fileSystemService.procesarImagenPrincipalListaColecciones(listaColeccionesDestacadasDTO);

        model.addAttribute("colecciones", listaColeccionesDestacadasDTO);
        model.addAttribute("hechos", listaHechosDestacadosDTO);
        model.addAttribute("titulo", "Inicio");
        return "index";
    }

    @GetMapping("/legales")
    public String legales(Model model) {
        model.addAttribute("titulo", "Información legal y Privacidad");
        return "legales";
    }

    @GetMapping("/about")
    public String aboutUs(Model model) {
        model.addAttribute("titulo", "Sobre Nosotros");
        return "about";
    }

    @GetMapping("/404")
    public String notFound(Model model) {
        model.addAttribute("titulo", "No encontrado");
        return "errores/404";
    }

    @GetMapping("/403")
    public String forbidden (Model model) {
        model.addAttribute("titulo", "Acceso Prohibido");
        return "errores/403";
    }

    @GetMapping("/500")
    public String internalError (Model model) {
        model.addAttribute("titulo", "Acceso Prohibido");
        return "errores/500";
    }
}
