package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoDinamicaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoMapaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.ContribuyenteOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechosController {
    private final IAgregadorService agregadorService;
    private final IFuenteDinamicaService fuenteDinamicaService;

    @Value("${app.hechos.page.size}")
    private Integer pageSize;

    @GetMapping
    public String listarHechos(@ModelAttribute HechosFilterInputDTO filtros, @RequestParam(value = "page", defaultValue = "0") int paginaActual, Model model) {
        if (filtros == null) {
            filtros = new HechosFilterInputDTO(); // para que Thymeleaf no rompa
        }

        List<HechoInputDTO> hechos = agregadorService.getAllHechos(paginaActual, filtros);
        List<FuenteInputDTO> fuentes = agregadorService.getFuentesPreview();

        model.addAttribute("titulo", String.format("Explorar - Pagina %d", paginaActual+1));
        model.addAttribute("hechos", hechos);
        model.addAttribute("fuentes", fuentes);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("filtros", filtros);
        model.addAttribute("rol", 1); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        return "/hechos/explore";
    }

    @GetMapping("/create")
    public String crearHecho(Model model) {
        HechoInputDTO hechoInputDTO = this.instanciarHecho();
        List<String> categorias = agregadorService.obtenerCategoriasShort();
        model.addAttribute("titulo", "Crear Hecho");
        model.addAttribute("hechoDTO", hechoInputDTO);
        model.addAttribute("categorias", categorias);
        model.addAttribute("rol", 1);
        model.addAttribute("logeado", 1);
        return "/hechos/create";
    }

    @PostMapping("/create") // path coincide con el form
    public String guardarHecho(@ModelAttribute("hechoDTO") HechoOutputDTO hechoDTO, Model model) {

        try {
            ContribuyenteOutputDTO contribuyenteOutputDTO = this.obtenerUsuarioPrueba();
            hechoDTO.setContribuyente(contribuyenteOutputDTO);
            int prueba = 0;
            fuenteDinamicaService.crearHecho(hechoDTO);

            return "redirect:/hechos";

        } catch (Exception e) {
            // Loguear error si quieres
            model.addAttribute("error", "No se pudo crear el hecho.");
            return "/hechos/create"; // Volver al form con mensaje de error
        }
    }


    @GetMapping("/{id}")
    public String hecho(@PathVariable("id") Long id, Model model) {
        try {
            HechoInputDTO hecho = agregadorService.getHechoById(id);
            ContribuyenteOutputDTO usuario = this.obtenerUsuarioPrueba();
            model.addAttribute("titulo", hecho.getTitulo());
            model.addAttribute("hecho", hecho);
            model.addAttribute("usuario", usuario);
            model.addAttribute("permitirEdicion",0);
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

    @GetMapping("/fuenteDinamica/{id}")
    public String obtenerHechoFuenteDinamica(@PathVariable("id") Long id, Model model) {
        HechoDinamicaInputDTO hecho = this.fuenteDinamicaService.obtenerHechoDinamicaId(id);

        model.addAttribute("titulo", "Hecho Fuente Dinamica");
        model.addAttribute("hecho", hecho);
        model.addAttribute("idContribuyente", 2);
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        return ""; //TODO agregar html de respuesta
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

    public HechoInputDTO instanciarHecho(){
        ContribuyenteInputDTO contribuyente = new ContribuyenteInputDTO();
        CategoriaInputDTO categoria = new CategoriaInputDTO();
        UbicacionInputDTO ubicacion = new UbicacionInputDTO();

        HechoInputDTO hecho = new HechoInputDTO();

        hecho.setContribuyente(contribuyente);
        hecho.setCategoria(categoria);
        hecho.setUbicacion(ubicacion);

        return hecho;
    }


    private  ContribuyenteOutputDTO obtenerUsuarioPrueba() {
        return ContribuyenteOutputDTO.builder()
                .nombre("Santiago")
                .apellido("Rodriguez")
                .fechaNacimiento(LocalDate.now())
                .id(17L)
                .build();
    }
}
