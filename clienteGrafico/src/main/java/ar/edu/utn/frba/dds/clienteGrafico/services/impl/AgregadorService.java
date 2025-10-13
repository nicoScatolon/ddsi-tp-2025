package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.DTOConverter;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoMapaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.dtoAuxiliares.ColeccionFormDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.EstadoDeSolicitud;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.ProcesarSolicitudOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgregadorService implements IAgregadorService {

    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String agregadorUrl;

    public AgregadorService(
            @Qualifier("agregadorWebClient") WebClient webClient,
            WebApiCallerService webApiCallerService,
            @Value("${servicio.agregador.url}") String agregadorUrl
    ) {
        this.webClient = webClient;
        this.webApiCallerService = webApiCallerService;
        this.agregadorUrl = agregadorUrl;
    }


    // --- HECHOS --- //

    public HechoInputDTO getHechoById(Long id) {

        try {
            return webApiCallerService.get(
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

        return webApiCallerService.getList(url, HechoInputDTO.class);
    }

    public List<HechoMapaInputDTO> getHechosMapa() {

        try {
            return webApiCallerService.getList(agregadorUrl + "/api/hechos/publica/mapa", HechoMapaInputDTO.class);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener el hecho mapa: " + e.getMessage(), e);
        }
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
                    agregadorUrl + "/api/colecciones/publica/editable/" + handle,
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

    // DESTACAR HECHOS
    @Override
    public List<HechoInputDTO> obtenerHechosDestacados() {
        try {
            return webApiCallerService.getList(
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
                    null,
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

    // DESTACAR COLECCION
    @Override
    public List<ColeccionPreviewInputDTO> obtenerColeccionesDestacadas() {
        try {
            return webApiCallerService.getList(
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
                    null,
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
    public List<ColeccionPreviewInputDTO> obtenerColeccionesPreview(Integer paginaActual) {
        String url = agregadorUrl + "/api/colecciones/publica/preview?page=" + paginaActual;

        try {
            return webApiCallerService.getList(url, ColeccionPreviewInputDTO.class);
        } catch (NotFoundException e) {
            throw new NotFoundException("colecciones preview", "página " + paginaActual);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener las colecciones preview: " + e.getMessage(), e);
        }
    }

    @Override
    public ColeccionPreviewInputDTO obtenerColeccionPreviewIndividual(String handle) {
        try {
            return webApiCallerService.get(
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

        return webApiCallerService.getList(url, HechoInputDTO.class);
    }
    // --- CATEGORIAS --- //

    @Override
    public List<String> obtenerCategoriasShort() {
        try {
            return webApiCallerService.getList(
                    agregadorUrl + "/api/privada/categorias/short",
                    String.class
            );
        } catch (NotFoundException e) {
            throw new NotFoundException("categorías", "short");
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener las categorías short: " + e.getMessage(), e);
        }
    }


    // --- SOLICITUDES ELIMINACION --- //

    @Override
    public ResponseEntity<Void> crearSolicitudEliminacion(Long hechoId, Long usuarioId, String razonEliminacion) {
        SolicitudEliminarHechoOutputDTO request =
                DTOConverter.convertirSolicitudEliminacion(hechoId, usuarioId, razonEliminacion);

        try {
            webApiCallerService.post(
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



    // --- FUENTES --- //

    @Override
    public List<FuenteInputDTO> getFuentesPreview() {
        try {
            return webApiCallerService.getList(
                    agregadorUrl + "/api/fuente/publica/preview",
                    FuenteInputDTO.class
            );
        } catch (NotFoundException e) {
            throw new NotFoundException("fuentes", "preview");
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener las fuentes preview: " + e.getMessage(), e);
        }
    }



    // --- METODOS PRIVADOS --- //

    private String construirUrlHechos(Integer paginaActual, HechosFilterOutputDTO filter) {
        return "/api/hechos/publica?page=" + paginaActual + construirQueryFiltros(filter);
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



    // --- TEST --- //
    public List<HechoInputDTO> obtenerHechos(Integer paginaActual) {
        //TODO temporal para test
        List<HechoInputDTO> hechos = new ArrayList<>();
        hechos.add(this.crearHecho1());
        hechos.add(this.crearHecho1());
        hechos.add(this.crearHecho1());
        return hechos;
    }

    private HechoInputDTO crearHecho1 (){
        return HechoInputDTO.builder()
                .id(Long.valueOf(1))
                .titulo("titulo 1")
                .descripcion("Lorem  vel nobis")
                .categoria(CategoriaInputDTO.builder()
                        .id("cat1")
                        .nombre("categoria 1")
                        .build())
                .ubicacion(UbicacionInputDTO.builder()
                        .provincia("Buenos Aires")
                        .departamento("CABA")                // Ciudad Autónoma de Buenos Aires
                        .calle("Av. Corrientes")          // Calle conocida
                        .numero(1234)
                        .latitud(-34.6037)                // Coordenadas aproximadas
                        .longitud(-58.3816)
                        .build())
                .fechaDeCarga(LocalDateTime.now())
                .fechaDeOcurrencia(LocalDateTime.now())
                .cargadoAnonimamente(true)
                .build();
    }
}
