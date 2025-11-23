package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas.MayorCategoriaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas.MayorProvPorColeccionInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas.PanelActividadViewDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas.SolicitudesSpamInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IEstadisticasFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstadisticasFacade implements IEstadisticasFacade {

    private final WebApiCallerService webApi;

    @Value("${servicio.estadisticas.url}")
    private String baseUrl;

    private String api(String path) {
        // baseUrl = http://servicio-estadisticas:8088
        return baseUrl + "/api/estadisticas" + path;
    }

    @Override
    public PanelActividadViewDTO getPanelActividad(String coleccion) {
        try {
            String urlMayorProvColeccion = api("/mayor_provincia_por_coleccion");
            if (coleccion != null && !coleccion.isBlank()) {
                urlMayorProvColeccion += "?handleColeccion=" +
                        URLEncoder.encode(coleccion, StandardCharsets.UTF_8);
            }

            String urlMayorCategoria = api("/mayor_categoria");
            String urlSolicitudesSpam = api("/solicitudes_de_spam");

            List<MayorProvPorColeccionInputDTO> listaProv =
                    webApi.getList(urlMayorProvColeccion, MayorProvPorColeccionInputDTO.class);

            List<MayorCategoriaInputDTO> listaCat =
                    webApi.getList(urlMayorCategoria, MayorCategoriaInputDTO.class);

            List<SolicitudesSpamInputDTO> listaSpam =
                    webApi.getList(urlSolicitudesSpam, SolicitudesSpamInputDTO.class);


            MayorProvPorColeccionInputDTO provDto =
                    (listaProv != null && !listaProv.isEmpty()) ? listaProv.get(0) : null;

            MayorCategoriaInputDTO catDto =
                    (listaCat != null && !listaCat.isEmpty()) ? listaCat.get(0) : null;

            SolicitudesSpamInputDTO spamDto =
                    (listaSpam != null && !listaSpam.isEmpty()) ? listaSpam.get(0) : null;

            String provinciaTop = (provDto != null) ? provDto.getProvincia() : null;
            Integer hechosProvTop = (provDto != null && provDto.getCantHechosProvincia() != null)
                    ? provDto.getCantHechosProvincia()
                    : 0;

            String categoriaTop = (catDto != null) ? catDto.getCategoria() : null;
            Integer hechosCatTop = (catDto != null && catDto.getCantHechosCategoria() != null)
                    ? catDto.getCantHechosCategoria()
                    : 0;

            Integer spam = (spamDto != null && spamDto.getSolicitudesSpam() != null)
                    ? spamDto.getSolicitudesSpam()
                    : 0;
            Integer noSpam = (spamDto != null && spamDto.getSolicitudesNoSpam() != null)
                    ? spamDto.getSolicitudesNoSpam()
                    : 0;

            return PanelActividadViewDTO.builder()
                    .provinciaTop(provinciaTop)
                    .hechosProvinciaTop(hechosProvTop)
                    .categoriaTop(categoriaTop)
                    .hechosCategoriaTop(hechosCatTop)
                    .solicitudesSpam(spam)
                    .solicitudesTotales(spam + noSpam)
                    .build();

        } catch (Exception e) {
            log.warn("Fallo armando panel actividad (coleccion={}): {}", coleccion, e.toString());

            return PanelActividadViewDTO.builder()
                    .provinciaTop(null)
                    .hechosProvinciaTop(0)
                    .categoriaTop(null)
                    .hechosCategoriaTop(0)
                    .solicitudesSpam(0)
                    .solicitudesTotales(0)
                    .build();
        }
    }
}
