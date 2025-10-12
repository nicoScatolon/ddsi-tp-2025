package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import ar.edu.utn.frba.dds.clienteGrafico.services.impl.AgregadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/solicitudesEliminacion")
@RequiredArgsConstructor
public class SolicitudesEliminacionController {
    private final IAgregadorService agregadorService;

    @PostMapping()
    public String solicitarEliminacion(
            @RequestParam String razonEliminacion,
            @RequestParam Long hechoId,
            @RequestParam Long usuarioId) {
        agregadorService.crearSolicitudEliminacion(hechoId, usuarioId, razonEliminacion);
        return "redirect:/about";
    }
}
