package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.ContenidoMultimediaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoDinamicaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoMapaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.ContribuyenteOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.ContenidoMultimediaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechoDinamicaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.TipoContenido;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFileSystemService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import ar.edu.utn.frba.dds.clienteGrafico.services.IGestionUsuariosService;
import ar.edu.utn.frba.dds.clienteGrafico.services.impl.WebApiCallerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechosController {
    private final IAgregadorService agregadorService;
    private final IFuenteDinamicaService fuenteDinamicaService;
    private final IFileSystemService fileSystemService;
    private final WebApiCallerService webApiCallerService;
    private final IGestionUsuariosService gestionUsuariosService;

    @Value("${app.hechos.page.size}")
    private Integer pageSize;

    @GetMapping
    public String listarHechos(@ModelAttribute HechosFilterInputDTO filtros, @RequestParam(value = "page", defaultValue = "0") int paginaActual, Model model) {
        if (filtros == null) {
            filtros = new HechosFilterInputDTO(); // para que Thymeleaf no rompa
        }

        List<HechoInputDTO> hechos = agregadorService.getAllHechos(paginaActual, filtros);
        List<FuenteInputDTO> fuentes = agregadorService.getFuentesPreview();

        fileSystemService.procesarImagenPrincipalListaHechos(hechos);

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

    @PostMapping("/create") //Todo si no estas logeado da error
    public String guardarHecho(
            @ModelAttribute("hechoDTO") HechoDinamicaOutputDTO hechoDTO,
            @RequestParam(value = "multimedia", required = false) List<MultipartFile> multimediaFiles,
            @RequestParam(value = "tipoContenido", required = false) List<String> tiposContenido,
            @RequestParam(value = "descripcionMultimedia", required = false) List<String> descripcionesMultimedia,  // ← Cambio aquí
            Model model,
            HttpSession session) {

        try {
            Long contribuyenteId = null;
            if (session != null) {
                contribuyenteId = (Long) session.getAttribute("userId");
            }
            hechoDTO.setContribuyenteId(contribuyenteId);
            // Procesar archivos multimedia
            if (multimediaFiles != null && !multimediaFiles.isEmpty()) {
                // Filtrar archivos vacíos
                List<MultipartFile> archivosValidos = new ArrayList<>();
                List<String> tiposValidos = new ArrayList<>();
                List<String> descripcionesValidas = new ArrayList<>();

                for (int i = 0; i < multimediaFiles.size(); i++) {
                    MultipartFile file = multimediaFiles.get(i);
                    if (!file.isEmpty() && file.getSize() > 0) {
                        archivosValidos.add(file);

                        if (tiposContenido != null && i < tiposContenido.size()) {
                            tiposValidos.add(tiposContenido.get(i));
                        }

                        // Convertir descripciones vacías a null
                        if (descripcionesMultimedia != null && i < descripcionesMultimedia.size()) {
                            String desc = descripcionesMultimedia.get(i);
                            String descripcionFinal = (desc == null || desc.trim().isEmpty()) ? null : desc.trim();
                            descripcionesValidas.add(descripcionFinal);
                        } else {
                            descripcionesValidas.add(null);
                        }
                    }
                }

                if (!archivosValidos.isEmpty()) {
                    System.out.println("Archivos válidos a procesar: " + archivosValidos.size());
                    List<ContenidoMultimediaOutputDTO> contenidoList =
                            fileSystemService.guardarContenidoMultimedia(
                                    archivosValidos,
                                    tiposValidos,
                                    descripcionesValidas
                            );
                    hechoDTO.setContenidoMultimedia(contenidoList);
                }
            }

            fuenteDinamicaService.crearHecho(hechoDTO);
            return "redirect:/hechos";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("esNuevo", true);
            model.addAttribute("error", "No se pudo crear el hecho: " + e.getMessage());
            model.addAttribute("categorias", agregadorService.obtenerCategoriasShort());
            model.addAttribute("provincias", Arrays.asList("Buenos Aires", "Córdoba", "Santa Fe"));
            return "hechos/create";
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

            if (hecho.getContenidoMultimedia() != null) {
                hecho.getContenidoMultimedia().forEach(contenido -> {
                    String urlCompleta = fileSystemService.construirUrlMultimedia(contenido.getUrl());
                    contenido.setUrl(urlCompleta);
                });
            }
            UsuarioInputDTO creadorHecho = new UsuarioInputDTO();
            if (hecho.getContribuyenteId() != null) {
                creadorHecho = gestionUsuariosService.obtenerUsuarioPorId(hecho.getContribuyenteId());
            }

            List<String> etiquetas = agregadorService.obtenerEtiquetasShort();

            model.addAttribute("titulo", hecho.getTitulo());
            model.addAttribute("hecho", hecho);
            model.addAttribute("etiquetas", etiquetas);
            model.addAttribute("contribuyente", creadorHecho);
            model.addAttribute("userId", userId);
            model.addAttribute("origenAgregador",true);
        } catch (NotFoundException e) {
            model.addAttribute("mensaje", "No se ha encontrado el hecho buscado");
            model.addAttribute("urlRedirect", "/hechos");
            return "errores/404";
        }
        return "hechos/details";
    }

    @PostMapping("/{id}/modificarEtiquetas")
    public String etiquetas(@PathVariable("id") Long id, @RequestBody List<String> etiquetas) {
        agregadorService.modificarEtiquetas(id, etiquetas);
        return "redirect:/hechos/"+id;
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

    @GetMapping("/map-all")
    @ResponseBody
    public List<HechoMapaInputDTO> getAllHechosMapa() {
        return agregadorService.getHechosMapa();
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

        if (hecho.getContenidoMultimedia() != null) {
            System.out.println("=== PROCESANDO MULTIMEDIA ===");
            System.out.println("Total items: " + hecho.getContenidoMultimedia().size());

            hecho.getContenidoMultimedia().forEach(contenido -> {
                System.out.println("URL ORIGINAL: " + contenido.getUrl());
                String urlCompleta = fileSystemService.construirUrlMultimedia(contenido.getUrl());
                System.out.println("URL COMPLETA: " + urlCompleta);
                contenido.setUrl(urlCompleta);
            });
        }

        ContribuyenteInputDTO creadorHecho = new ContribuyenteInputDTO();
        creadorHecho.setId(hecho.getContribuyenteId());
        creadorHecho.setNombre("AAAA");
        creadorHecho.setApellido("BBBB");
        //TODO obtener datos del usuario con el id desde el hecho

        model.addAttribute("titulo", "Dinamica: "+ hecho.getTitulo());
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
        List<String> provincias = agregadorService.obtenerProvinciasShort();

        // Crear lista de multimedia con URLs completas SOLO para la vista
        List<Map<String, Object>> multimediaParaVista = new ArrayList<>();

        if (hecho.getContenidoMultimedia() != null && !hecho.getContenidoMultimedia().isEmpty()) {
            for (int i = 0; i < hecho.getContenidoMultimedia().size(); i++) {
                ContenidoMultimediaInputDTO contenido = hecho.getContenidoMultimedia().get(i);

                Map<String, Object> mediaMap = new HashMap<>();
                mediaMap.put("id", contenido.getId());
                mediaMap.put("tipoContenido", contenido.getTipoContenido().toString());
                mediaMap.put("descripcion", contenido.getDescripcion() != null ? contenido.getDescripcion() : "");
                mediaMap.put("url", contenido.getUrl()); // URL ORIGINAL (relativa)
                mediaMap.put("urlCompleta", fileSystemService.construirUrlMultimedia(contenido.getUrl())); // URL completa para vista
                mediaMap.put("index", i);

                multimediaParaVista.add(mediaMap);
            }
        }

        model.addAttribute("actionUrl", "/hechos/editar");
        model.addAttribute("esNuevo", false);
        model.addAttribute("categorias", categorias);
        model.addAttribute("provincias", provincias);
        model.addAttribute("hechoDTO", hecho);
        model.addAttribute("multimediaExistente", multimediaParaVista);
        model.addAttribute("titulo", "Editar Hecho");

        return "hechos/create";
    }

    @PutMapping("/editar")
    public String guardarEditarHecho(
            @ModelAttribute("hechoDTO") HechoDinamicaOutputDTO hechoDTO,
            @RequestParam(value = "multimedia", required = false) List<MultipartFile> multimediaFiles,
            @RequestParam(value = "tipoContenido", required = false) List<String> tiposContenido,
            @RequestParam(value = "descripcionMultimedia", required = false) List<String> descripcionesMultimedia,
            // Parámetros para multimedia existente
            @RequestParam(value = "multimediaExistenteIds", required = false) String multimediaExistenteIds,
            @RequestParam(value = "multimediaExistenteUrls", required = false) String multimediaExistenteUrls,
            @RequestParam(value = "multimediaExistenteTipos", required = false) String multimediaExistenteTipos,
            @RequestParam(value = "multimediaExistenteDescs", required = false) String multimediaExistenteDescs,
            @RequestParam(value = "multimediaEliminarIds", required = false) String multimediaEliminarIds,
            Model model,
            HttpSession session) {

        try {
            Long userId = null;
            if (session != null) {
                userId = (Long) session.getAttribute("userId");
            }
            hechoDTO.setContribuyenteId(userId);


            // Lista final de multimedia
            List<ContenidoMultimediaOutputDTO> multimediaFinal = new ArrayList<>();

            // PROCESAR MULTIMEDIA EXISTENTE
            Set<Long> idsAEliminar = new HashSet<>();
            if (multimediaEliminarIds != null && !multimediaEliminarIds.isEmpty()) {
                String[] idsArray = multimediaEliminarIds.split(",");
                for (String id : idsArray) {
                    if (!id.trim().isEmpty()) {
                        idsAEliminar.add(Long.parseLong(id.trim()));
                    }
                }
            }

            if (multimediaExistenteIds != null && !multimediaExistenteIds.isEmpty()) {
                String[] ids = multimediaExistenteIds.split("\\|\\|\\|");
                String[] urls = multimediaExistenteUrls != null ? multimediaExistenteUrls.split("\\|\\|\\|") : new String[0];
                String[] tipos = multimediaExistenteTipos != null ? multimediaExistenteTipos.split("\\|\\|\\|") : new String[0];
                String[] descs = multimediaExistenteDescs != null ? multimediaExistenteDescs.split("\\|\\|\\|", -1) : new String[0];


                for (int i = 0; i < ids.length; i++) {
                    Long mediaId = Long.parseLong(ids[i].trim());

                    // Si NO está marcado para eliminar, mantenerlo
                    if (!idsAEliminar.contains(mediaId)) {
                        ContenidoMultimediaOutputDTO contenido = new ContenidoMultimediaOutputDTO();
                        contenido.setId(mediaId);
                        contenido.setUrl(i < urls.length ? urls[i].trim() : ""); // URL ORIGINAL
                        contenido.setTipoContenido(i < tipos.length ? TipoContenido.valueOf(tipos[i].trim()) : TipoContenido.IMAGEN);
                        contenido.setDescripcion(i < descs.length && !descs[i].trim().isEmpty() ? descs[i].trim() : null);

                        multimediaFinal.add(contenido);
                    } else {
                        // fileSystemService.eliminarArchivo(urls[i]); //Todo
                    }
                }
            }

            // PROCESAR NUEVOS ARCHIVOS
            if (multimediaFiles != null && !multimediaFiles.isEmpty()) {
                List<MultipartFile> archivosValidos = new ArrayList<>();
                List<String> tiposValidos = new ArrayList<>();
                List<String> descripcionesValidas = new ArrayList<>();

                for (int i = 0; i < multimediaFiles.size(); i++) {
                    MultipartFile file = multimediaFiles.get(i);
                    if (!file.isEmpty() && file.getSize() > 0) {
                        archivosValidos.add(file);

                        if (tiposContenido != null && i < tiposContenido.size()) {
                            tiposValidos.add(tiposContenido.get(i));
                        } else {
                            tiposValidos.add("IMAGEN");
                        }

                        if (descripcionesMultimedia != null && i < descripcionesMultimedia.size()) {
                            String desc = descripcionesMultimedia.get(i);
                            String descripcionFinal = (desc == null || desc.trim().isEmpty()) ? null : desc.trim();
                            descripcionesValidas.add(descripcionFinal);
                        } else {
                            descripcionesValidas.add(null);
                        }
                    }
                }

                if (!archivosValidos.isEmpty()) {
                    List<ContenidoMultimediaOutputDTO> contenidoNuevo =
                            fileSystemService.guardarContenidoMultimedia(
                                    archivosValidos,
                                    tiposValidos,
                                    descripcionesValidas
                            );

                    // Agregar los nuevos a la lista final
                    multimediaFinal.addAll(contenidoNuevo);
                }
            }

            // Asignar la lista final al DTO
            hechoDTO.setContenidoMultimedia(multimediaFinal);

            // Guardar los cambios
            fuenteDinamicaService.editarHecho(hechoDTO);

            return "redirect:/hechos/fuenteDinamica/" + hechoDTO.getId();

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "No se pudo editar el hecho: " + e.getMessage());
            model.addAttribute("esNuevo", false);
            model.addAttribute("categorias", agregadorService.obtenerCategoriasShort());
            model.addAttribute("provincias", agregadorService.obtenerProvinciasShort());
            return "hechos/create";
        }
    }

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
