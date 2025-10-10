package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPanelController {
    @GetMapping
    public String adminPanel(){
        return "redirect:/admin/hechos";
    }

    @GetMapping("/actividad")
    public String resumenActividad(Model model) {
        model.addAttribute("titulo", "Resumen Actividad");
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        model.addAttribute("contentTemplate", "actividad");
        return "admin/panel-base";
    }

    @GetMapping("/importar")
    public String importarHechos(Model model) {
        model.addAttribute("titulo", "Importar Hechos");
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        model.addAttribute("contentTemplate", "importar-hechos");
        return "admin/panel-base";
    }

    @GetMapping("/hechos")
    public String gestionHechos(Model model) {
        model.addAttribute("titulo", "Gestión de Hechos");
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        model.addAttribute("contentTemplate", "gestion-hechos");
        return "admin/panel-base";
    }

    @GetMapping("/colecciones")
    public String gestionColecciones(Model model) {
        model.addAttribute("titulo", "Gestión de Colecciones");
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        model.addAttribute("contentTemplate", "gestion-colecciones");
        return "admin/panel-base";
    }

    @GetMapping("/solicitudes")
    public String solicitudesEliminacion(Model model) {
        model.addAttribute("titulo", "Gestión de Solicitudes Eliminación");
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        model.addAttribute("contentTemplate", "solicitudes-eliminacion");
        return "admin/panel-base";
    }

}
