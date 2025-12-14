package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.DTOConverter;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoMapaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.FiltroConsenso;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Fuentes.FuenteOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.CategoriaEquivalenteOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechosFilterOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.EstadoDeSolicitud;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.ProcesarSolicitudOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AgregadorService implements IAgregadorService {
    private final WebApiCallerService webApiCallerService;
    private final String agregadorUrl;

    public AgregadorService(
            WebApiCallerService webApiCallerService,
            @Value("${servicio.agregador.url}") String agregadorUrl
    ) {
        this.webApiCallerService = webApiCallerService;
        this.agregadorUrl = agregadorUrl;
    }


    // --- HECHOS --- //

    public HechoInputDTO getHechoById(Long id) {

        try {
            return webApiCallerService.getPublic(
                    agregadorUrl + "/api/hechos/publica/" + id,
                    HechoInputDTO.class
            );
        } catch (NotFoundException e) {
            throw new NotFoundException("hecho", id.toString());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener el hecho " + id + ": " + e.getMessage(), e);
        }
    }

    public List<HechoInputDTO> getAllHechos(Integer paginaActual, HechosFilterInputDTO filterInputDTO) {
        HechosFilterOutputDTO filter = DTOConverter.convertirHechosFilterInputDTO(filterInputDTO);
        String url = construirUrlHechos(paginaActual, filter);

        return webApiCallerService.getPublicList(url, HechoInputDTO.class);
    }

    @Cacheable("hechos-mapa")
    public List<HechoMapaInputDTO> getHechosMapa() {
        try {
            return webApiCallerService.getPublicList(agregadorUrl + "/api/hechos/publica/mapa", HechoMapaInputDTO.class);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener el hecho mapa: " + e.getMessage(), e);
        }
    }

    @Override
    @Cacheable(value = "hechosPorProvincia", key = "#provincia")
    public List<HechoMapaInputDTO> getHechosMapaPorProvincia(String provincia) {
        try {
            String url = agregadorUrl + "/api/hechos/publica/mapa?provincia=" + provincia;
            return webApiCallerService.getPublicList(url, HechoMapaInputDTO.class);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener los hechos del mapa por provincia: " + e.getMessage(), e);
        }
    }

    @Scheduled(fixedRate = 30 * 60 * 1000) // cada 30 minutos
    @CacheEvict(value = {"hechos-mapa", "hechosPorProvincia"}, allEntries = true)
    public void limpiarCacheHechos() {
        System.out.println("Cache de hechos invalidado");
    }


    // --- COLECCIONES --- //

    @Override
    public ResponseEntity<Void> crearColeccion(ColeccionOutputDTO coleccionDTO) {
        try {
            webApiCallerService.post(
                    agregadorUrl + "/api/colecciones/privada",
                    coleccionDTO,
                    Void.class
            );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al crear la colección: " + e.getMessage(), e);
        }
    }

    public ResponseEntity<Void> eliminarColeccion(String handle) {
        try {
            webApiCallerService.delete(agregadorUrl + "/api/colecciones/privada/" + handle);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al eliminar la colección con handle " + handle + ": " + e.getMessage(), e);
        }
    }

    public ColeccionInputDTO obtenerColeccion(String handle) {
        try {
            return webApiCallerService.get(
                    agregadorUrl + "/api/colecciones/privada/editable/" + handle,
                    ColeccionInputDTO.class
            );
        } catch (NotFoundException e) {
            throw new NotFoundException("colección", handle);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener la colección " + handle + ": " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> editarColeccion(ColeccionOutputDTO coleccionOutputDTO) {
        try {
            webApiCallerService.put(
                    agregadorUrl + "/api/colecciones/privada",
                    coleccionOutputDTO,
                    Void.class
            );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al editar la colección: " + e.getMessage(), e);
        }
    }

    public List<ColeccionOutputDTO> obtenerColeccionesAdmin() {
        try {
            return webApiCallerService.getList(
                    agregadorUrl + "/api/colecciones/privada",
                    ColeccionOutputDTO.class
            );
        } catch (RuntimeException e) {
            throw new RuntimeException(
                    "Error al obtener las colecciones para admin: " + e.getMessage(), e
            );
        }
    }

    // --- DESTACAR HECHOS --- //
    @Override
    public List<HechoInputDTO> obtenerHechosDestacados() {
        try {
            return webApiCallerService.getPublicList(
                    agregadorUrl + "/api/hechos/publica/destacados",
                    HechoInputDTO.class
            );
        } catch (NotFoundException e) {
            throw new NotFoundException("hechos destacados", "");
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener los hechos destacados: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> destacarHecho(Long id) {
        try {
            webApiCallerService.put(
                    agregadorUrl + "/api/hechos/privada/destacado/" + id,
                    "",
                    Void.class
            );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al destacar el hecho con id " + id + ": " + e.getMessage(), e);
        }
    }


    @Override
    public ResponseEntity<Void> eliminarDestacarHecho(Long id) {
        try {
            webApiCallerService.delete(
                    agregadorUrl + "/api/hechos/privada/destacado/" + id
            );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al eliminar destacado del hecho con id " + id + ": " + e.getMessage(), e);
        }
    }

    // --- DESTACAR COLECCION --- //
    @Override
    public List<ColeccionPreviewInputDTO> obtenerColeccionesDestacadas() {
        try {
            return webApiCallerService.getPublicList(
                    agregadorUrl + "/api/colecciones/publica/destacadas",
                    ColeccionPreviewInputDTO.class
            );
        } catch (NotFoundException e) {
            throw new NotFoundException("colecciones destacadas", "");
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener las colecciones destacadas: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> destacarColeccion(String handle) {
        try {
            webApiCallerService.put(
                    agregadorUrl + "/api/colecciones/privada/destacada/" + handle,
                    0, //El "0" es para que no tire error el webApiCallerService que espera un body, pero el back no necesita, no es recomendable pero funciona
                    Void.class
            );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al destacar la colección con handle " + handle + ": " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> eliminarDestacarColeccion(String handle) {
        try {
            webApiCallerService.delete(
                    agregadorUrl + "/api/colecciones/privada/destacada/" + handle
            );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al eliminar destacado de la colección con handle " + handle + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<CategoriaEquivalenteInputDTO> obtenerCatEquivalentes() {
        try {
            return webApiCallerService.getList(
                    agregadorUrl + "/api/privada/categorias/equivalentes",
                    CategoriaEquivalenteInputDTO.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las categorías equivalentes: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> crearEquivalencia(CategoriaEquivalenteOutputDTO categoria) {
        try {
            webApiCallerService.post(
                    agregadorUrl + "/api/privada/categorias/equivalentes",
                    categoria,
                    CategoriaEquivalenteInputDTO.class
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la categorías equivalente: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> eliminarEquivalencia(String categoria) {
        try {
            webApiCallerService.delete(
                    agregadorUrl + "/api/privada/categorias/equivalentes/" + categoria
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la categorías equivalente: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> editarEquivalencia(CategoriaEquivalenteOutputDTO categoria) {
        try {
            webApiCallerService.put(
                    agregadorUrl + "/api/privada/categorias/equivalentes",
                    categoria,
                    CategoriaEquivalenteInputDTO.class
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la categorías equivalente: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> modificarEtiquetas(Long id, List<String> etiquetas) {
        try {
            webApiCallerService.post(
                    agregadorUrl + "/api/hechos/privada/"+id+"/etiquetas",
                    etiquetas,
                    String.class
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar las etiquetas: " + e.getMessage(), e);
        }
    }



    @Override
    public List<ColeccionPreviewInputDTO> obtenerColeccionesPreview(Integer paginaActual, FiltroConsenso filtroConsenso) {
        String url = getString(paginaActual, filtroConsenso);

        try {
            return webApiCallerService.getPublicList(url, ColeccionPreviewInputDTO.class);
        } catch (NotFoundException e) {
            throw new NotFoundException("colecciones preview", "página " + paginaActual);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener las colecciones preview: " + e.getMessage(), e);
        }
    }

    private String getString(Integer paginaActual, FiltroConsenso filtroConsenso) {
        String url = agregadorUrl + "/api/colecciones/publica/preview";

        if (paginaActual != null) {
            url = url + "?page=" + paginaActual;
        }

        if (filtroConsenso == null || filtroConsenso == FiltroConsenso.TODOS) {
            return url;
        }

        if (filtroConsenso == FiltroConsenso.SIN_CONSENSO) {
            return url + "&consenso=NINGUNO";
        }

        return url + "&consenso=" + filtroConsenso.name();
    }


    @Override
    public ColeccionPreviewInputDTO obtenerColeccionPreviewIndividual(String handle) {
        try {
            return webApiCallerService.getPublic(
                    agregadorUrl + "/api/colecciones/publica/preview/" + handle,
                    ColeccionPreviewInputDTO.class
            );
        } catch (NotFoundException e) {
            throw new NotFoundException("colección preview", handle);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener la colección preview con handle " + handle + ": " + e.getMessage(), e);
        }
    }


    public List<HechoInputDTO> obtenerHechosColeccion(String handle, Integer paginaActual,  HechosFilterInputDTO filtros, Boolean curado) {
        HechosFilterOutputDTO filter = DTOConverter.convertirHechosFilterInputDTO(filtros);
        String url = construirUrlHechosColeccion(handle, paginaActual, filter, curado);

        return webApiCallerService.getPublicList(url, HechoInputDTO.class);
    }
    // --- CATEGORIAS --- //

    @Override
    public List<String> obtenerCategoriasShort() {
        try {
            String raw = webApiCallerService.getPublic(
                    agregadorUrl + "/api/privada/categorias/short",
                    String.class
            );
            //Esto lo tuve q implementar porq devolvía todas las categorías en una sola posición
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(raw, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las categorías short: " + e.getMessage(), e);
        }
    }


    @Override
    public  List<String> obtenerEtiquetasShort() {
        try {
            String raw = webApiCallerService.getPublic(
                    agregadorUrl + "/api/hechos/publica/etiquetas",
                    String.class
            );
            //Esto lo tuve q implementar porq devolvía todas las categorías en una sola posición
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(raw, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las etiquetas short: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CategoriaInputDTO> obtenerCategorias() {
        try {
            return webApiCallerService.getPublicList(
                    agregadorUrl + "/api/privada/categorias",
                    CategoriaInputDTO.class
            );
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener las categorías: " + e.getMessage(), e);
        }
    }

    @Override
    public List<EtiquetaInputDTO> obtenerEtiquetas() {
        return List.of(); //Todo
    }

    @Override
    public List<String> obtenerProvinciasShort() { //Todo, Pienso q se le pida al agregador aunq no es necesario
        return List.of(
                "Buenos Aires",
                "Catamarca",
                "Chaco",
                "Chubut",
                "Ciudad Autónoma de Buenos Aires",
                "Córdoba",
                "Corrientes",
                "Entre Ríos",
                "Formosa",
                "Jujuy",
                "La Pampa",
                "La Rioja",
                "Mendoza",
                "Misiones",
                "Neuquén",
                "Río Negro",
                "Salta",
                "San Juan",
                "San Luis",
                "Santa Cruz",
                "Santa Fe",
                "Santiago del Estero",
                "Tierra del Fuego",
                "Tucumán"
        );
    }



    // --- SOLICITUDES ELIMINACION --- //
    @Override
    public ResponseEntity<Void> crearSolicitudEliminacion(Long hechoId, Long usuarioId, String razonEliminacion) {
        SolicitudEliminarHechoOutputDTO request =
                DTOConverter.convertirSolicitudEliminacion(hechoId, usuarioId, razonEliminacion);

        try {
            webApiCallerService.postPublic(
                    agregadorUrl + "/api/solicitudes-eliminacion/publica",
                    request,
                    Void.class
            );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al crear la solicitud de eliminación: " + e.getMessage(), e);
        }
    }

    @Override
    public List<SolicitudEliminarHechoInputDTO> obtenerSolicitudesEliminacionPendientes() {
        try {
            return webApiCallerService.getList(
                    agregadorUrl + "/api/solicitudes-eliminacion/privada",
                    SolicitudEliminarHechoInputDTO.class
            );
        } catch (NotFoundException e) {
            throw new NotFoundException("solicitudes de eliminación pendientes", "");
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener las solicitudes de eliminación pendientes: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> gestionarSolicitud(ProcesarSolicitudOutputDTO procesarSolicitudOutputDTO,
                                                   EstadoDeSolicitud estadoDeSolicitud) {
        try {
            String url = agregadorUrl + "/api/solicitudes-eliminacion/privada/solicitud?accion=" + estadoDeSolicitud;

            webApiCallerService.post(
                    url,
                    procesarSolicitudOutputDTO,
                    Void.class
            );

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al gestionar la solicitud de eliminación: " + e.getMessage(), e);
        }
    }

    @Override
    public List<SolicitudEliminarHechoInputDTO> obtenerSolicitudesEliminacionUsuario (Long usuarioId) {
        try {
            String url = agregadorUrl + "/api/solicitudes-eliminacion/publica?idCreador=" + usuarioId;

            return webApiCallerService.getList(
                    url,
                    SolicitudEliminarHechoInputDTO.class
            );
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al gestionar la solicitud de eliminación: " + e.getMessage(), e);
        }
    }


    // --- FUENTES --- //

    @Override
    public List<FuenteInputDTO> getFuentesPreview() {
        try {
            return webApiCallerService.getPublicList(
                    agregadorUrl + "/api/fuente/publica/preview",
                    FuenteInputDTO.class
            );
        } catch (NotFoundException e) {
            throw new NotFoundException("fuentes", "preview");
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener las fuentes preview: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> crearCategoria(CategoriaOutputDTO categoria) {
        try {
            String url = agregadorUrl + "/api/privada/categorias";

            webApiCallerService.post(
                    url,
                    categoria,
                    Void.class
            );

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al crear la categoria: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> editarCategoria(CategoriaOutputDTO categoria) {
        try {
            String url = agregadorUrl + "/api/privada/categorias";

            webApiCallerService.put(
                    url,
                    categoria,
                    Void.class
            );

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al editar la categoria: " + e.getMessage(), e);
        }
    }

    // --- METODOS ADMIN SUPERIOR --- //

    @Override
    public ResponseEntity<Void> actualizarFuentesForzosamente() {
        try {
            String url = agregadorUrl + "/api/fuente/privada/actualizar";
            webApiCallerService.get( url, Void.class );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al actualizar las fuentes: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> actualizarColeccionesForzosamente() {
        try {
            String url = agregadorUrl + "/api/colecciones/privada/actualizar";
            webApiCallerService.get( url, Void.class );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al actualizar las colecciones: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> curarColeccionesForzosamente() {
        try {
            String url = agregadorUrl + "/api/colecciones/privada/curar";
            webApiCallerService.get( url, Void.class );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al curar las colecciones: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> crearFuente(FuenteOutputDTO fuenteDTO) {
        try {
            String url = agregadorUrl + "/api/fuente/privada";

            webApiCallerService.put(
                    url,
                    fuenteDTO,
                    Void.class
            );

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> eliminarFuente(Long id) {
        try {
            webApiCallerService.delete(agregadorUrl + "/api/fuente/privada/" + id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al eliminar la fuente con id " + id + ": " + e.getMessage(), e);
        }
    }

    // --- METODOS PRIVADOS --- //

    private String construirUrlHechos(Integer paginaActual, HechosFilterOutputDTO filter) {
        return agregadorUrl + "/api/hechos/publica?page=" + paginaActual + construirQueryFiltros(filter);
    }

    private String construirUrlHechosColeccion(String handle, Integer paginaActual,
                                               HechosFilterOutputDTO filter, Boolean curado) {
        return agregadorUrl + "/api/colecciones/publica/" + handle
                + "/hechos?curado=" + (curado != null ? curado : false)
                + "&page=" + paginaActual
                + construirQueryFiltros(filter);
    }

    /**
     * Construye el fragmento de query con los filtros dinámicos (sin el '?')
     */
    private String construirQueryFiltros(HechosFilterOutputDTO filter) {
        StringBuilder sb = new StringBuilder();

        if (filter.getCategoria() != null && !filter.getCategoria().isEmpty()) {
            sb.append("&categoria=").append(filter.getCategoria());
        }
        if (filter.getProvincia() != null && !filter.getProvincia().isEmpty()) {
            sb.append("&provincia=").append(filter.getProvincia());
        }
        if (filter.getEtiqueta() != null && !filter.getEtiqueta().isEmpty()) {
            sb.append("&etiqueta=").append(filter.getEtiqueta());
        }
        if (filter.getFuenteId() != null) {
            sb.append("&fuenteId=").append(filter.getFuenteId());
        }
        if (filter.getFReporteDesde() != null) {
            sb.append("&fReporteDesde=").append(filter.getFReporteDesde());
        }
        if (filter.getFReporteHasta() != null) {
            sb.append("&fReporteHasta=").append(filter.getFReporteHasta());
        }
        if (filter.getFAconDesde() != null) {
            sb.append("&fAconDesde=").append(filter.getFAconDesde());
        }
        if (filter.getFAconHasta() != null) {
            sb.append("&fAconHasta=").append(filter.getFAconHasta());
        }

        return sb.toString();
    }

}
