package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.EstadoHecho;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoDinamicaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.UsuarioInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Usuarios.RegisterUsuarioRequestDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Usuarios.UsuarioOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IGestionUsuariosService;
import lombok.RequiredArgsConstructor;
import ar.edu.utn.frba.dds.clienteGrafico.services.impl.WebApiCallerService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsuarioController {
    private final IFuenteDinamicaService fuenteDinamicaService;
    private final IAgregadorService agregadorService;
    private final IGestionUsuariosService gestionUsuariosService;

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final WebClient web = WebClient.builder().build();

    private final WebApiCallerService webApiCallerService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "unauthorized", required = false) String unauthorized,
                        Model model) {
        model.addAttribute("titulo", "Iniciar Sesión");

        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
        }

        if (unauthorized != null) {
            model.addAttribute("info", "Debes iniciar sesión para acceder a esa página.");
        }

        return "usuario/login";
    }


    @GetMapping("/signup")
    public String signup(Model model) {
        RegisterUsuarioRequestDTO request = new RegisterUsuarioRequestDTO();
        model.addAttribute("request", request);
        model.addAttribute("titulo", "Registrarse");
        return "usuario/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("request") RegisterUsuarioRequestDTO request, Model model) {
        // Validación de contraseñas
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            model.addAttribute("request", request);
            model.addAttribute("titulo", "Registrarse");
            return "usuario/signup";
        }

        try {
            gestionUsuariosService.crearUsuario(request);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("request", request);
            return "usuario/signup";
        }
        return "redirect:/login"; // Mejor redirigir al login después del registro
    }


    @GetMapping("/profile/{id}")
    public String perfil(@PathVariable("id") Long id, Model model, HttpSession session) {
        UsuarioInputDTO usuario = gestionUsuariosService.obtenerUsuarioPorId(id);

        model.addAttribute("usuario", usuario);
        model.addAttribute("edad", calcularEdad(usuario.getFecha_nacimiento()));
        model.addAttribute("titulo", "Perfil - " +  usuario.getNombre() +" "+ usuario.getApellido());
        model.addAttribute("editor", session.getAttribute("userId").equals(id)); //Si el usuario es el mismo que el perfil, lo puede editar
        return "usuario/profile";
    }

    @GetMapping("/profile")
    public String miPerfil(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("userId");

        return "redirect:/profile/" + usuarioId;
    }

    @PostMapping("/profile")
    public String perfil(
            @ModelAttribute UsuarioOutputDTO usuario,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("userId");

        try{
            gestionUsuariosService.actualizarUsuario(userId,usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado con éxito ✅");

        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile/"+userId;
    }

    @PostMapping("/profile/change-password")
    public String changePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes){
        Long userId = (Long) session.getAttribute("userId");

        try{
            UsuarioOutputDTO usuarioDTO = UsuarioOutputDTO.builder()
                    .currentPassword(currentPassword)
                    .newPassword(newPassword)
                    .confirmNewPassword(confirmPassword)
                    .build();
            gestionUsuariosService.actualizarUsuario(userId,usuarioDTO);
            redirectAttributes.addFlashAttribute("success","Contraseña actualizada correctamente 🔐");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile/"+userId;
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
        return "usuario/mis-hechos";
    }

    @GetMapping("/mis-solicitudes")
    public String misSolicitudes(Model model, HttpSession session) {

        Long usuarioId = (Long) session.getAttribute("userId");

        List<SolicitudEliminarHechoInputDTO> solicitudes = this.agregadorService.obtenerSolicitudesEliminacionUsuario(usuarioId);
        //if (solicitudes == null) {solicitudes = new ArrayList<>();}

        model.addAttribute("usuarioId", usuarioId);
        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("titulo", "Mis Hechos");
        return "usuario/mis-solicitudes";
    }

    // --- PRIVADO --- //

    private Integer calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return null; // o lanzar excepción según tu lógica
        }
        LocalDate hoy = LocalDate.now();
        return Period.between(fechaNacimiento, hoy).getYears();
    }
}
