package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.UsuarioOutputDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.Period;

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
