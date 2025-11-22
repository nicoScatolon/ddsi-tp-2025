package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.DTOConverter;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.EstadoHecho;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoDinamicaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.RevisionHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Fuentes.FuenteOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.CategoriaEquivalenteOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.EstadoDeSolicitud;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.ProcesarSolicitudOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Usuarios.RegisterUsuarioRequestDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFileSystemService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IGestionUsuariosService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPanelController {
    private final IAgregadorService agregadorService;
    private final IFuenteDinamicaService fuenteDinamicaService;
    private final IFileSystemService fileSystemService;
    private final IGestionUsuariosService gestionUsuariosService;

    @Value("${app.colecciones.page.size}")
    private Integer pageSize;

    @GetMapping
    public String adminPanel(){
        return "redirect:/admin/actividad";
    }

    // Resumen actividad
    @GetMapping("/actividad")
    public String resumenActividad(Model model) {
        model.addAttribute("titulo", "Resumen Actividad");
        model.addAttribute("contentTemplate", "actividad");
        return "admin/panel-base";
    }

    // Importar archivos
    @GetMapping("/importar")
    public String importarHechos(Model model) {
        model.addAttribute("titulo", "Importar Hechos");
        model.addAttribute("contentTemplate", "importar-hechos");
        return "admin/panel-base";
    }

    @PostMapping("/importar")
    public String procesarImportacion(
            @RequestParam("archivo") MultipartFile archivo,
            RedirectAttributes redirectAttributes) {

        try {
            fileSystemService.importarArchivoCSV(archivo);

            redirectAttributes.addFlashAttribute("success",
                    "Archivo importado exitosamente: " + archivo.getOriginalFilename());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al guardar el archivo: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error inesperado: " + e.getMessage());
        }

        return "redirect:/admin/importar";
    }

    //Gestión de hechos
    @GetMapping("/hechos")
    public String gestionHechos(@RequestParam(required = false) EstadoHecho estado, Model model) {
        if (estado == null) {estado = EstadoHecho.PENDIENTE;}
        List<HechoDinamicaInputDTO> hechos = this.fuenteDinamicaService.obtenerHechosDinamica(estado);

        model.addAttribute("estado", estado);
        model.addAttribute("titulo", "Gestión de Hechos");
        model.addAttribute("hechos", hechos);
        model.addAttribute("contentTemplate", "gestion-hechos");
        return "admin/panel-base";
    }

    @PostMapping("/hechos") //Todo la sugerencia la hacemos aca porq agregar el botón dentro del hecho-detail hay q modificar muchos controllers
    public String gestionarHechosDinamica(@ModelAttribute RevisionHechoInputDTO revisionHecho, HttpSession session){
        Long adminId = (Long) session.getAttribute("userId");
        this.fuenteDinamicaService.enviarRevisionHechoDinamica(revisionHecho, adminId);
        return "redirect:/admin/hechos";
    }

    // Gestión de colecciones
    @GetMapping("/colecciones")
    public String gestionColecciones(@RequestParam(value = "page", defaultValue = "0") int paginaActual, Model model) {
        List<ColeccionPreviewInputDTO> colecciones = agregadorService.obtenerColeccionesPreview(paginaActual, null);
        model.addAttribute("colecciones", colecciones);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("pageSize", pageSize);

        model.addAttribute("titulo", "Gestión de Colecciones");
        model.addAttribute("contentTemplate", "gestion-colecciones");
        return "admin/panel-base";
    }

    //Gestión de categorias
     @GetMapping("/categorias")
     public String gestionCategorias(Model model) {
        model.addAttribute("categorias", agregadorService.obtenerCategorias());
        model.addAttribute("equivalentes", agregadorService.obtenerCatEquivalentes());

        model.addAttribute("titulo", "Gestión de Categorías");
        model.addAttribute("contentTemplate", "gestion-categorias");
        //Todo estaría bueno q este paginado
        return "admin/panel-base";
     }

    @PostMapping("/categorias")
    public String crearCategoria(@ModelAttribute CategoriaOutputDTO categoria) {
        agregadorService.crearCategoria(categoria);

        return "redirect:/admin/categorias";
    }

    @PutMapping("/categorias")
    public String editarCategoria(@ModelAttribute CategoriaOutputDTO categoria){
        agregadorService.editarCategoria(categoria);
        return "redirect:/admin/categorias";
    }

    @PostMapping("/categorias/equivalente")
    public String crearEquivalencia(@ModelAttribute CategoriaEquivalenteOutputDTO categoria) {
        agregadorService.crearEquivalencia(categoria);

        return "redirect:/admin/categorias";
    }

    @PutMapping("/categorias/equivalente")
    public String editarEquivalencia(@ModelAttribute CategoriaEquivalenteOutputDTO categoria) {
        agregadorService.editarEquivalencia(categoria);

        return "redirect:/admin/categorias";
    }

    @DeleteMapping("/categorias/equivalente/{nombre}")
    public String crearEquivalencia(@PathVariable String nombre) {
        agregadorService.eliminarEquivalencia(nombre);

        return "redirect:/admin/categorias";
    }



    //Gestión solicitudes de eliminación
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

    // Acciones para Admin Superior

    @GetMapping("/adminsuperior")
    public String accionesAdminSuperior(Model model) {
        List<FuenteInputDTO> fuentes = agregadorService.getFuentesPreview();

        model.addAttribute("titulo", "Acciones Avanzadas");
        model.addAttribute("fuentes", fuentes);
        model.addAttribute("contentTemplate", "acciones-avanzadas");

        return "admin/panel-base";
    }

    @PostMapping("/adminsuperior/fuentes/actualizar")
    public String actualizarFuenteForzosamente() {
        agregadorService.actualizarFuentesForzosamente();
        return "redirect:/admin/adminsuperior";
    }

    @PostMapping("/adminsuperior/fuentes")
    public String crearFuente(@ModelAttribute FuenteOutputDTO fuenteDTO, RedirectAttributes redirectAttributes) {
        try {
            agregadorService.crearFuente(fuenteDTO);
            return "redirect:/admin/adminsuperior";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/adminsuperior";
        }
    }

    @DeleteMapping("/adminsuperior/fuentes/{id}")
    public String eliminarFuente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            agregadorService.eliminarFuente(id);
            return "redirect:/admin/adminsuperior";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/adminsuperior";
        }
    }

    @PostMapping("/adminsuperior/colecciones/actualizar")
    public String actualizarColeccionesForzosamente() {
        agregadorService.actualizarColeccionesForzosamente();
        return "redirect:/admin/adminsuperior";
    }

    @PostMapping("/adminsuperior/colecciones/curar")
    public String curarColeccionesForzosamente() {
        agregadorService.curarColeccionesForzosamente();
        return "redirect:/admin/adminsuperior";
    }

    @PostMapping("/crear-admin")
    public String crearAdmin(@ModelAttribute RegisterUsuarioRequestDTO usuario, Model model) {
        // Validación de contraseñas en el frontend
        if (!usuario.getPassword().equals(usuario.getConfirmPassword())) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            model.addAttribute("titulo", "Acciones Avanzadas");
            model.addAttribute("contentTemplate", "acciones-avanzadas");
            return "admin/panel-base";
        }
        try {
            gestionUsuariosService.crearAdmin(usuario);
            return "redirect:/admin/adminsuperior";

        } catch (IllegalArgumentException e) {
            // Errores de validación del backend (400)
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", "Acciones Avanzadas");
            model.addAttribute("contentTemplate", "acciones-avanzadas");
            return "admin/panel-base";

        } catch (Exception e) {
            // Errores inesperados
            model.addAttribute("error", "Error inesperado: " + e.getMessage());
            model.addAttribute("titulo", "Acciones Avanzadas");
            model.addAttribute("contentTemplate", "acciones-avanzadas");
            return "admin/panel-base";
        }
    }
}
