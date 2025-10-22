package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoDinamicaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoMapaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.ContribuyenteOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechoDinamicaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import jakarta.servlet.http.HttpSession;
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

        List<String> categorias = agregadorService.obtenerCategoriasShort();
        List<String> provincias = agregadorService.obtenerProvinciasShort();
        List<String> etiquetas = agregadorService.obtenerEtiquetasShort();  //Todo podrian ser unicamente las etiquetas de la coleccion, ahora manda todas


        model.addAttribute("titulo", String.format("Explorar - Pagina %d", paginaActual+1));
        model.addAttribute("etiquetas", etiquetas);
        model.addAttribute("hechos", hechos);
        model.addAttribute("fuentes", fuentes);
        model.addAttribute("categorias", categorias);
        model.addAttribute("provincias", provincias);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("filtros", filtros);
        return "/hechos/explore";
    }

    @GetMapping("/create")
    public String crearHecho(Model model) {
        HechoInputDTO hechoInputDTO = this.instanciarHecho();
        List<String> categorias = agregadorService.obtenerCategoriasShort();
        List<String> provincias = agregadorService.obtenerProvinciasShort();

        model.addAttribute("actionUrl", "/hechos/create");
        model.addAttribute("esNuevo", true);
        model.addAttribute("titulo", "Crear Hecho");
        model.addAttribute("hechoDTO", hechoInputDTO);
        model.addAttribute("provincias", provincias);
        model.addAttribute("categorias", categorias);
        return "/hechos/create";
    }

    @PostMapping("/create") // path coincide con el form
    public String guardarHecho(@ModelAttribute("hechoDTO") HechoDinamicaOutputDTO hechoDTO, Model model, HttpSession session) {

        try {
            Long contribuyenteId = null;
            if (session != null) {
                contribuyenteId = (Long) session.getAttribute("userId"); //TODO ver para cargar hechos sin tener cuenta, si era posible o no y como recibe back
            }
            hechoDTO.setContribuyenteId(contribuyenteId);
            fuenteDinamicaService.crearHecho(hechoDTO);

            return "redirect:/hechos";
            //return "redirect:/hechos/" + hechoDTO.getId(); //Todo q fuenteDinamica devuelva el id, y podremos redirecionar al hecho q se envió

        } catch (Exception e) {
            // Loguear error si quieres
            model.addAttribute("esNuevo", true);
            model.addAttribute("error", "No se pudo crear el hecho.");
            return "/hechos/create"; // Volver al form con mensaje de error
        }
    }


    @GetMapping("/{id}")
    public String hecho(@PathVariable("id") Long id, Model model, HttpSession session) {
        try {
            HechoInputDTO hecho = agregadorService.getHechoById(id);

            Long userId = null;
            if (session != null) {
                userId = (Long) session.getAttribute("userId");
            }

            ContribuyenteInputDTO creadorHecho = new ContribuyenteInputDTO();
            creadorHecho.setId(hecho.getContribuyenteId());
            creadorHecho.setNombre("AAAA");
            creadorHecho.setApellido("BBBB");
            //TODO obtener datos del usuario con el id desde el hecho --> pedir al servicio de usuarios el nombre, apellido linkeados al contribuyenteId que tiene el hecho

            model.addAttribute("titulo", hecho.getTitulo());
            model.addAttribute("hecho", hecho);
            model.addAttribute("contribuyente", creadorHecho);
            model.addAttribute("userId", userId);
            model.addAttribute("origenAgregador",true);
        } catch (NotFoundException e) {
            model.addAttribute("mensaje", "No se ha encontrado el hecho buscado");
            model.addAttribute("urlRedirect", "/hechos");
            return "404";
        }
        return "hechos/details";
    }

    @PutMapping("/destacar/{id}")
    public String destacarHecho(@PathVariable("id") Long id, Model model){
        agregadorService.destacarHecho(id);

        return "redirect:/hechos/" + id;
    }

    @DeleteMapping("/destacar/{id}")
    public String eliminarDestacarHecho(@PathVariable("id") Long id, Model model){
        agregadorService.eliminarDestacarHecho(id);

        return "redirect:/hechos/" + id;
    }

    @GetMapping("/map")
    public String mapaHechos(Model model) {
        List<HechoMapaInputDTO> hechosMapa = agregadorService.getHechosMapa();
        model.addAttribute("hechosMapa", hechosMapa);
        model.addAttribute("titulo", "Mapa de Hechos");
        return "hechos/map";
    }

    @GetMapping("/fuenteDinamica/{id}")
    public String obtenerHechoFuenteDinamica(@PathVariable("id") Long id, Model model, HttpSession session) {
        HechoDinamicaInputDTO hecho = this.fuenteDinamicaService.obtenerHechoDinamicaId(id);

        if (hecho == null) {
            model.addAttribute("mensaje", "No se ha encontrado el hecho buscado");
            model.addAttribute("urlRedirect", "/user/misHechos");
            return "404";
        }

        Long userId = null;
        if (session != null) {
            userId = (Long) session.getAttribute("userId");
        }

        ContribuyenteInputDTO creadorHecho = new ContribuyenteInputDTO();
        creadorHecho.setId(hecho.getContribuyenteId());
        creadorHecho.setNombre("AAAA");
        creadorHecho.setApellido("BBBB");
        //TODO obtener datos del usuario con el id desde el hecho

        model.addAttribute("titulo", "Hecho Fuente Dinamica");
        model.addAttribute("hecho", hecho);
        model.addAttribute("contribuyente", creadorHecho);
        model.addAttribute("userId", userId);
        model.addAttribute("origenAgregador",false);
        return "hechos/details";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        HechoDinamicaInputDTO hecho = fuenteDinamicaService.obtenerHechoDinamicaId(id);
        List<String> categorias = agregadorService.obtenerCategoriasShort();

        model.addAttribute("actionUrl", "/hechos/editar");
        model.addAttribute("esNuevo", false);
        model.addAttribute("categorias", categorias);
        model.addAttribute("hechoDTO", hecho);
        model.addAttribute("titulo", "Editar Hecho");
        return "hechos/create";
    }

    @PutMapping("/editar")
    public String guardarEditarHecho(@ModelAttribute("hechoDTO") HechoDinamicaOutputDTO hechoDTO, Model model, HttpSession session) {
        try {
            Long userId = null;
            if (session != null) {
                userId = (Long) session.getAttribute("userId");
            }
            hechoDTO.setContribuyenteId(userId); //seteo el valor de quien lo esta modificando actualmente
            fuenteDinamicaService.editarHecho(hechoDTO);

            return "redirect:/hechos/fuenteDinamica/" + hechoDTO.getId(); //Todo q fuenteDinamica devuelva el id, y podremos redirecionar al hecho q se envió

        } catch (Exception e) {
            // Loguear error si quieres
            model.addAttribute("error", "No se pudo editar el hecho.");
            return "redirect:/hechos/mis-hechos"; // Volver al form con mensaje de error
        }
    }
    /*
     List<HechoMapaInputDTO> hechosMapa = new ArrayList<>();
            hechosMapa.add(crearHecho(1L, "Robo en el centro", "Seguridad", -34.6037, -58.3816));
            hechosMapa.add(crearHecho(2L, "Incendio en depósito", "Emergencia", -34.6111, -58.3775));
            hechosMapa.add(crearHecho(3L, "Corte de luz", "Servicios", -34.6083, -58.3700));
            hechosMapa.add(crearHecho(4L, "Manifestación pacífica", "Social", -34.5952, -58.3829));
            hechosMapa.add(crearHecho(5L, "Fuga de gas", "Infraestructura", -34.6205, -58.3850));
    */

    public HechoInputDTO instanciarHecho(){
        CategoriaInputDTO categoria = new CategoriaInputDTO();
        UbicacionInputDTO ubicacion = new UbicacionInputDTO();

        HechoInputDTO hecho = new HechoInputDTO();

        hecho.setContribuyenteId(null);
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
