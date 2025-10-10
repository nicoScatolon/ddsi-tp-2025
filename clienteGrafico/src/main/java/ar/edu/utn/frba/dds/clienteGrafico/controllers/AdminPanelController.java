package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.DTOConverter;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.EstadoDeSolicitud;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.ProcesarSolicitudOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPanelController {
    private final IAgregadorService agregadorService;

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
        List<SolicitudEliminarHechoInputDTO> solicitudes = agregadorService.obtenerSolicitudesEliminacionPendientes();
        //Todo deberiamos obtener los usuarios asociados a cada solicitud por el servicio de usuarios
        model.addAttribute("titulo", "Gestión de Solicitudes Eliminación");
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        model.addAttribute("contentTemplate", "solicitudes-eliminacion");
        model.addAttribute("solicitud", new SolicitudEliminarHechoInputDTO());
        model.addAttribute("solicitudes", solicitudes);
        return "admin/panel-base";
    }

    @PostMapping("/solicitudes/aceptar")
    public String aceptarSolicitud(@ModelAttribute  SolicitudEliminarHechoInputDTO solicitud) {
        //Todo obtener id admin
        ProcesarSolicitudOutputDTO procesarSolicitudOutputDTO = DTOConverter.convertirProcesarSolicitudOutputDTO(solicitud, 17L);
        agregadorService.gestionarSolicitud(procesarSolicitudOutputDTO, EstadoDeSolicitud.ACEPTADA);

        return "redirect:/admin/solicitudes";
    }

    @PostMapping("/solicitudes/rechazar")
    public String rechazarSolicitud(@ModelAttribute  SolicitudEliminarHechoInputDTO solicitud) {
        //Todo obtener id admin
        ProcesarSolicitudOutputDTO procesarSolicitudOutputDTO = DTOConverter.convertirProcesarSolicitudOutputDTO(solicitud, 17L);
        agregadorService.gestionarSolicitud(procesarSolicitudOutputDTO, EstadoDeSolicitud.RECHAZADA);

        return "redirect:/admin/solicitudes";
    }
}
