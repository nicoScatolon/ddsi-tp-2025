package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.UsuarioOutputDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {

    @GetMapping("/login")
    public String login(Model model) {
        UsuarioOutputDTO usuario = new UsuarioOutputDTO();
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Iniciar Sesión");
        return "usuario/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        UsuarioOutputDTO usuario = new UsuarioOutputDTO();
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Registrarse");
        return "usuario/signup";
    }
}
