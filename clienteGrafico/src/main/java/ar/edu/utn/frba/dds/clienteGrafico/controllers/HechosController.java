package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoMapaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.impl.AgregadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechosController {
    private final AgregadorService agregadorService;

    @Value("${app.hechos.page.size}")
    Integer pageSize;

    @GetMapping
    public String listarHechos(@RequestParam(value = "page", defaultValue = "0") int paginaActual, Model model) {
        List<HechoInputDTO> hechos = agregadorService.getAllHechos(paginaActual);
        model.addAttribute("titulo", String.format("Explorar - Pagina %d", paginaActual+1));
        model.addAttribute("hechos", hechos);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        return "/hechos/explore";
    }

    @GetMapping("/{id}")
    public String hecho(@PathVariable("id") Long id, Model model) {
        try {
            HechoInputDTO hecho = agregadorService.getHechoById(id);
            model.addAttribute("titulo", hecho.getTitulo());
            model.addAttribute("hecho", hecho);
            model.addAttribute("rol", 2); // TODO temporal mientras no tenemos roles/usuarios
            model.addAttribute("logeado", 1);
        } catch (NotFoundException e) {
            model.addAttribute("hecho", null);
        }
        return "/hechos/hecho-detail";
    }

    @GetMapping("/map")
    public String mapaHechos(Model model) {
        List<HechoMapaInputDTO> hechosMapa = agregadorService.getHechosMapa();
        model.addAttribute("hechosMapa", hechosMapa);
        model.addAttribute("titulo", "Mapa de Hechos");
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        return "/hechos/hechos-map";
    }

    /*
     List<HechoMapaInputDTO> hechosMapa = new ArrayList<>();
            hechosMapa.add(crearHecho(1L, "Robo en el centro", "Seguridad", -34.6037, -58.3816));
            hechosMapa.add(crearHecho(2L, "Incendio en depósito", "Emergencia", -34.6111, -58.3775));
            hechosMapa.add(crearHecho(3L, "Corte de luz", "Servicios", -34.6083, -58.3700));
            hechosMapa.add(crearHecho(4L, "Manifestación pacífica", "Social", -34.5952, -58.3829));
            hechosMapa.add(crearHecho(5L, "Fuga de gas", "Infraestructura", -34.6205, -58.3850));
    */

    private HechoMapaInputDTO crearHecho(Long id, String titulo, String categoria, Double lat, Double lng) {
        HechoMapaInputDTO dto = new HechoMapaInputDTO();
        dto.setId(id);
        dto.setTitulo(titulo);
        dto.setCategoria(categoria);
        dto.setLatitud(lat);
        dto.setLongitud(lng);
        return dto;
    }
}
