package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas.MayorProvPorCatInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IEstadisticasFacade;
import ar.edu.utn.frba.dds.clienteGrafico.services.impl.WebApiCallerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class EstadisticasFacade implements IEstadisticasFacade {

    private final WebApiCallerService webApi;

    @Value("${services.estadisticas.base-url}")
    private String baseUrl; // ej: http://servicio-estadisticas:8080/api/estadisticas

    @Override
    public PanelActividadViewDTO getPanelActividad(String coleccion) {
        try {
            // Endpoints del micro (ajustá a tus rutas reales)
            String urlMayorProvCat       = baseUrl + "/mayor-provincia-por-categoria?coleccion=" + coleccion;
            String urlMayorProvColeccion = baseUrl + "/mayor-provincia-por-coleccion?coleccion=" + coleccion;
            String urlSolicitudesSpam    = baseUrl + "/solicitudes-eliminacion?coleccion=" + coleccion;

            // Llamadas HTTP
            List<MayorProvPorCatInputDTO> mayorPorCat =
                    webApi.getList(urlMayorProvCat, MayorProvPorCatInputDTO.class);

            MayorProvPorColeccionInputDTO mayorPorColeccion =
                    webApi.getPublic(urlMayorProvColeccion, MayorProvPorColeccionInputDTO.class);

            SolicitudesSpamInputDTO spamRaw =
                    webApi.getPublic(urlSolicitudesSpam, SolicitudesSpamInputDTO.class);

            // Derivados para la vista
            List<ParClaveValorDTO> porCategoria = (mayorPorCat == null ? Collections.<MayorProvPorCatInputDTO>emptyList() : mayorPorCat)
                    .stream()
                    .map(x -> new ParClaveValorDTO(
                            (x != null && x.getCategoriaDTO() != null && x.getCategoriaDTO().getNombre() != null)
                                    ? x.getCategoriaDTO().getNombre()
                                    : "(sin categoría)",
                            (x != null && x.getCantHechosProvincia() != null)
                                    ? x.getCantHechosProvincia()
                                    : 0
                    ))
                    .collect(Collectors.toList());

            List<ParClaveValorDTO> porProvincia = (mayorPorColeccion == null)
                    ? Collections.emptyList()
                    : List.of(new ParClaveValorDTO(
                    (mayorPorColeccion.getProvincia() != null) ? mayorPorColeccion.getProvincia() : "",
                    (mayorPorColeccion.getCantHechosProvincia() != null) ? mayorPorColeccion.getCantHechosProvincia() : 0
            ));

            int spam   = (spamRaw != null && spamRaw.getSolicitudesSpam() != null)     ? spamRaw.getSolicitudesSpam()     : 0;
            int noSpam = (spamRaw != null && spamRaw.getSolicitudesNoSpam() != null)   ? spamRaw.getSolicitudesNoSpam()   : 0;

            return PanelActividadViewDTO.builder()
                    .porProvincia(porProvincia)
                    .porCategoria(porCategoria)
                    .spamEliminacion(new SpamEliminacionDTO(spam, spam + noSpam))
                    .build();

        } catch (Exception e) {
            log.warn("Fallo armando panel actividad (coleccion={}): {}", coleccion, e.toString());
            return PanelActividadViewDTO.builder()
                    .porProvincia(Collections.emptyList())
                    .porCategoria(Collections.emptyList())
                    .spamEliminacion(new SpamEliminacionDTO(0, 0))
                    .build();
        }
    }
}
