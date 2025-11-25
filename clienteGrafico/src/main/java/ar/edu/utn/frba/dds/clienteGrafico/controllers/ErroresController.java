package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.services.IGestionUsuariosService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/error")
@RequiredArgsConstructor
public class ErroresController implements ErrorController {
    private final IGestionUsuariosService gestionUsuariosService;

    @RequestMapping
    public String handleError(HttpServletRequest request, Model model, HttpSession session) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            System.out.println(">>> ERROR STATUS CODE: " + statusCode);

            if (statusCode == 400) {
                return error400(model, session);
            } else if (statusCode == 403) {
                return error403(model, session);
            } else if (statusCode == 404) {
                return error404(model, session);
            } else if (statusCode == 500) {
                return error500(model, session);
            }
        }

        // Por defecto, error 500
        return error500(model, session);
    }

    @GetMapping("/400")
    public String error400(Model model, HttpSession session) {
        String userName = gestionUsuariosService.obtenerUsername(session);

        model.addAttribute("userName", userName);
        model.addAttribute("titulo", "Error 400");
        model.addAttribute("errorCode", "400");
        model.addAttribute("errorTitle", "Solicitud Incorrecta");
        model.addAttribute("errorMessage", "Los parámetros de la URL son inválidos o están mal formateados. Por favor, verifica e intenta nuevamente.");
        return "layout/base-error";
    }

    @GetMapping("/404")
    public String error404(Model model, HttpSession session) {
        String userName = gestionUsuariosService.obtenerUsername(session);

        model.addAttribute("userName", userName);
        model.addAttribute("titulo", "Error 404");
        model.addAttribute("errorCode", "404");
        model.addAttribute("errorTitle", "Página No Encontrada");
        model.addAttribute("errorMessage", "La página que buscas no existe o ha sido movida. Verifica la URL o regresa al inicio.");
        return "layout/base-error";
    }

    @GetMapping("/500")
    public String error500(Model model, HttpSession session) {
        String userName = gestionUsuariosService.obtenerUsername(session);

        model.addAttribute("userName", userName);
        model.addAttribute("titulo", "Error 500");
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorTitle", "Error Interno del Servidor");
        model.addAttribute("errorMessage", "Ocurrió un error inesperado en nuestros servidores. Por favor, intenta nuevamente más tarde.");
        return "layout/base-error";
    }

    @GetMapping("/403")
    public String error403(Model model, HttpSession session) {
        String userName = gestionUsuariosService.obtenerUsername(session);

        model.addAttribute("userName", userName);
        model.addAttribute("titulo", "Error 403");
        model.addAttribute("errorCode", "403");
        model.addAttribute("errorTitle", "Acceso Prohibido");
        model.addAttribute("errorMessage", "No tienes permisos para acceder a este recurso. Si crees que esto es un error, contacta al administrador.");
        return "layout/base-error";
    }
}