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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        model.addAttribute("provincias",this.ObtenerProvincias());
        model.addAttribute("listaProvincias",  agregadorService.obtenerProvinciasShort());
        model.addAttribute("titulo", "Mapa de Hechos");
        return "hechos/map";
    }

    @GetMapping("/mapa-por-provincia")
    @ResponseBody
    public List<HechoMapaInputDTO> getHechosMapaPorProvincia(@RequestParam String provincia) {
        return agregadorService.getHechosMapaPorProvincia(provincia);
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

    private Map<String, Map<String, Double>> ObtenerProvincias(){
        Map<String, Map<String, Double>> provincias = new LinkedHashMap<>();

        provincias.put("Buenos Aires", Map.of("lat", -34.6037, "lng", -58.3816));
        provincias.put("Córdoba", Map.of("lat", -31.4201, "lng", -64.1888));
        provincias.put("Santa Fe", Map.of("lat", -31.6333, "lng", -60.7000));
        provincias.put("Mendoza", Map.of("lat", -32.8895, "lng", -68.8458));
        provincias.put("Tucumán", Map.of("lat", -26.8083, "lng", -65.2176));
        provincias.put("Salta", Map.of("lat", -24.7859, "lng", -65.4117));
        provincias.put("Entre Ríos", Map.of("lat", -31.7333, "lng", -60.5297));
        provincias.put("Misiones", Map.of("lat", -27.3621, "lng", -55.9008));
        provincias.put("Corrientes", Map.of("lat", -27.4692, "lng", -58.8306));
        provincias.put("Chaco", Map.of("lat", -27.4514, "lng", -58.9867));
        provincias.put("Santiago del Estero", Map.of("lat", -27.7834, "lng", -64.2642));
        provincias.put("Jujuy", Map.of("lat", -24.1858, "lng", -65.2995));
        provincias.put("Catamarca", Map.of("lat", -28.4696, "lng", -65.7795));
        provincias.put("La Rioja", Map.of("lat", -29.4131, "lng", -66.8558));
        provincias.put("San Juan", Map.of("lat", -31.5375, "lng", -68.5364));
        provincias.put("San Luis", Map.of("lat", -33.2950, "lng", -66.3356));
        provincias.put("Neuquén", Map.of("lat", -38.9516, "lng", -68.0591));
        provincias.put("Río Negro", Map.of("lat", -40.8135, "lng", -62.9967));
        provincias.put("Chubut", Map.of("lat", -43.2951, "lng", -65.1091));
        provincias.put("Santa Cruz", Map.of("lat", -48.8154, "lng", -69.9611));
        provincias.put("Tierra del Fuego", Map.of("lat", -54.8019, "lng", -68.3029));
        provincias.put("Formosa", Map.of("lat", -26.1775, "lng", -58.1781));
        provincias.put("La Pampa", Map.of("lat", -36.6167, "lng", -64.2833));
        return provincias;
    }
}
