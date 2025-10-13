package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.EstadoHecho;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoDinamicaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.UsuarioOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import lombok.RequiredArgsConstructor;
import ar.edu.utn.frba.dds.clienteGrafico.services.impl.GestionUsuariosService;
import ar.edu.utn.frba.dds.clienteGrafico.services.impl.WebApiCallerService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UsuarioController {
    private final IFuenteDinamicaService fuenteDinamicaService;

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final WebClient web = WebClient.builder().build();

    private final WebApiCallerService webApiCallerService;

    @GetMapping("/login")
    public String login(Model model) {
        UsuarioOutputDTO usuario = new UsuarioOutputDTO();
        model.addAttribute("usuario", usuario); //TODO ver que es usuario
        model.addAttribute("titulo", "Iniciar Sesión");
        return "usuario/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        UsuarioOutputDTO usuario = new UsuarioOutputDTO();
        model.addAttribute("usuario", usuario); //TODO ver que es usuario
        model.addAttribute("titulo", "Registrarse");
        return "usuario/signup";
    }

    @GetMapping("/profile/{id}")
    public String perfil(@PathVariable("id") Long id, Model model) {
        UsuarioOutputDTO usuario = usuarioPrueba(); //Todo obtener el usuario por id
        Integer edad = calcularEdad(usuario.getFechaNacimiento());
        model.addAttribute("usuario", usuario); // Todo: Mock, si no coinciden no se puede editar
        model.addAttribute("perfil", usuario); //TODO ver esto con nico
        model.addAttribute("edad", edad);
        model.addAttribute("titulo", "Perfil");
        model.addAttribute("editor", true); //TODO ver que es esto de editor
        return "usuario/profile";
    }

    @GetMapping("/mis-hechos")
    public String misHechos(
            @RequestParam(required = false) EstadoHecho estadoHecho,
            @RequestParam(value = "page", defaultValue = "0") int paginaActual,
            Model model,
            HttpSession session) {

        Long usuarioId = (Long) session.getAttribute("userId");

        List<HechoDinamicaInputDTO> hechos = this.fuenteDinamicaService.obtenerHechosDinamicaUsuario(usuarioId, estadoHecho, paginaActual);
        //if (hechos == null) {hechos = new ArrayList<>();}

        model.addAttribute("usuarioId", usuarioId);
        model.addAttribute("hechos", hechos);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("estadoHecho", estadoHecho != null ? estadoHecho.name() : "");
        model.addAttribute("titulo", "Mis Hechos");
        model.addAttribute("editor", true); //TODO ver que es editor
        return "usuario/mis-hechos";
    }



    private Integer calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return null; // o lanzar excepción según tu lógica
        }
        LocalDate hoy = LocalDate.now();
        return Period.between(fechaNacimiento, hoy).getYears();
    }


    private UsuarioOutputDTO usuarioPrueba(){
        return UsuarioOutputDTO.builder()
                .nombre("Usuario")
                .apellido("Usuario")
                .email("Usuario")
                .password("Usuario")
                .fechaNacimiento(LocalDate.now())
                .build();
    }
}
