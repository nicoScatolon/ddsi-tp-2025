package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.DTOConverter;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.EstadoHecho;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoDinamicaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.RevisionHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.EstadoDeSolicitud;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.ProcesarSolicitudOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPanelController {
    private final IAgregadorService agregadorService;
    private final IFuenteDinamicaService fuenteDinamicaService;

    @Value("${app.colecciones.page.size}")
    private Integer pageSize;

    @GetMapping
    public String adminPanel(){
        return "redirect:/admin/hechos";
    }

    @GetMapping("/actividad")
    public String resumenActividad(Model model) {
        model.addAttribute("titulo", "Resumen Actividad");
        model.addAttribute("contentTemplate", "actividad");
        return "admin/panel-base";
    }

    @GetMapping("/importar")
    public String importarHechos(Model model) {
        model.addAttribute("titulo", "Importar Hechos");
        model.addAttribute("contentTemplate", "importar-hechos");
        return "admin/panel-base";
    }

    @GetMapping("/hechos")
    public String gestionHechos(@RequestParam(required = false) EstadoHecho estado, Model model) {
        if (estado == null) {estado = EstadoHecho.PENDIENTE;}
        List<HechoDinamicaInputDTO> hechos = this.fuenteDinamicaService.obtenerHechosDinamica(estado);

        model.addAttribute("titulo", "Gestión de Hechos");
        model.addAttribute("hechos", hechos);
        model.addAttribute("contentTemplate", "gestion-hechos");
        return "admin/panel-base";
    }

    @PostMapping("/hechos")
    public String gestionarHechosDinamica(@ModelAttribute RevisionHechoInputDTO revisionHecho, HttpSession session){
        Long adminId = (Long) session.getAttribute("userId");
        this.fuenteDinamicaService.enviarRevisionHechoDinamica(revisionHecho, adminId);
        return "redirect:/admin/hechos";
    }

    @GetMapping("/colecciones")
    public String gestionColecciones(@RequestParam(value = "page", defaultValue = "0") int paginaActual, Model model) {
        List<ColeccionPreviewInputDTO> colecciones = agregadorService.obtenerColeccionesPreview(paginaActual);
        model.addAttribute("colecciones", colecciones);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("pageSize", pageSize);

        model.addAttribute("titulo", "Gestión de Colecciones");
        model.addAttribute("contentTemplate", "gestion-colecciones");
        return "admin/panel-base";
    }

    @GetMapping("/solicitudes")
    public String solicitudesEliminacion(Model model) {
        List<SolicitudEliminarHechoInputDTO> solicitudes = agregadorService.obtenerSolicitudesEliminacionPendientes();
        //Todo deberiamos obtener los usuarios asociados a cada solicitud por el servicio de usuarios
        model.addAttribute("titulo", "Gestión de Solicitudes Eliminación");
        model.addAttribute("contentTemplate", "solicitudes-eliminacion");
        model.addAttribute("solicitudes", solicitudes);
        return "admin/panel-base";
    }

    @PostMapping("/solicitudes/aceptar")
    public String aceptarSolicitud(@ModelAttribute  SolicitudEliminarHechoOutputDTO solicitud, HttpSession session) {
        Long adminId = (Long) session.getAttribute("userId");

        ProcesarSolicitudOutputDTO procesarSolicitudOutputDTO = DTOConverter.convertirProcesarSolicitudOutputDTO(solicitud, adminId);
        agregadorService.gestionarSolicitud(procesarSolicitudOutputDTO, EstadoDeSolicitud.ACEPTADA);

        return "redirect:/admin/solicitudes";
    }

    @PostMapping("/solicitudes/rechazar")
    public String rechazarSolicitud(@ModelAttribute SolicitudEliminarHechoOutputDTO solicitud, HttpSession session) {
        Long adminId = (Long) session.getAttribute("userId");

        ProcesarSolicitudOutputDTO procesarSolicitudOutputDTO = DTOConverter.convertirProcesarSolicitudOutputDTO(solicitud, adminId);
        agregadorService.gestionarSolicitud(procesarSolicitudOutputDTO, EstadoDeSolicitud.RECHAZADA);

        return "redirect:/admin/solicitudes";
    }
}
