package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.UsuarioOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.impl.WebApiCallerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

@Controller
public class UsuarioController {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final WebClient web = WebClient.builder().build();

    private final WebApiCallerService webApiCallerService;

    public UsuarioController(WebApiCallerService webApiCallerService) {
        this.webApiCallerService = webApiCallerService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        UsuarioOutputDTO usuario = new UsuarioOutputDTO();
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Iniciar Sesión");
        return "usuario/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("usuario") UsuarioOutputDTO usuario, HttpSession session) {
        // del usuario para el login solo se carga su email y password
        try {

            Map<String, String> body = java.util.Map.of("username", usuario.getEmail(), "password", usuario.getPassword());
            AuthResponseDTO tokens = web.post().uri(authServiceUrl + "/auth")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();

            if (tokens == null || tokens.getAccessToken() == null) {
                return "redirect:/login";
            }

            session.setAttribute("accessToken", tokens.getAccessToken());
            session.setAttribute("refreshToken", tokens.getRefreshToken());

            return "redirect:/index";

        }
        catch (Exception e) {
            //todo: mensaje personalizado para el error de credencial inválida
            return "redirect:/login";
        }
    }


    @GetMapping("/signup")
    public String signup(Model model) {
        UsuarioOutputDTO usuario = new UsuarioOutputDTO();
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Registrarse");
        return "usuario/signup";
    }

    @GetMapping("/profile/{id}")
    public String perfil(@PathVariable("id") Long id, Model model) {
        UsuarioOutputDTO usuario = usuarioPrueba(); //Todo obtener el usuario por id
        Integer edad = calcularEdad(usuario.getFechaNacimiento());
        model.addAttribute("usuario", usuario); // Todo: Mock, si no coinciden no se puede editar
        model.addAttribute("perfil", usuario);
        model.addAttribute("edad", edad);
        model.addAttribute("titulo", "Perfil");
        model.addAttribute("editor", true);
        return "usuario/profile";
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
