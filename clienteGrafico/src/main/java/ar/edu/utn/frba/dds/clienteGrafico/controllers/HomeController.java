package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.UsuarioInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFileSystemService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IGestionUsuariosService;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final IAgregadorService agregadorService;
    private final IFileSystemService fileSystemService;
    private final IGestionUsuariosService gestionUsuariosService;
    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        List<ColeccionPreviewInputDTO> listaColeccionesDestacadasDTO = agregadorService.obtenerColeccionesDestacadas();
        List<HechoInputDTO> listaHechosDestacadosDTO = agregadorService.obtenerHechosDestacados();

        fileSystemService.procesarImagenPrincipalListaHechos(listaHechosDestacadosDTO);
        fileSystemService.procesarImagenPrincipalListaColecciones(listaColeccionesDestacadasDTO);

        Long userId = null;
        if (session != null) {
            userId = (Long) session.getAttribute("userId");
        }
        String userName = null;
        if (userId != null) {
            UsuarioInputDTO usuarioInputDTO = gestionUsuariosService.obtenerUsuarioPorId(userId);
            userName = usuarioInputDTO.getUsername();
        }

        model.addAttribute("userName", userName);
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


}
