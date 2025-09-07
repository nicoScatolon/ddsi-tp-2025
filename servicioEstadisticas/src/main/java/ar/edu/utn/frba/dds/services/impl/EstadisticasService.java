package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOconverter;
import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Estadisticas.*;
import ar.edu.utn.frba.dds.domain.entities.Hecho;
import ar.edu.utn.frba.dds.domain.entities.SolicitudEliminacion;
import ar.edu.utn.frba.dds.services.ICategoriasService;
import ar.edu.utn.frba.dds.services.IEstadisticasService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstadisticasService implements IEstadisticasService {
    //TODO repository
    private final ICategoriasService categoriasService;

    private final WebClient webClient;

    @Value("agregador.base-url")
    private String urlAgregador;
    @Value("diasPersistentesEstadisticas")
    private Integer cantDiasPersistenciaEstaditicas;

    public EstadisticasService(ICategoriasService categoriasService) {
        this.categoriasService = categoriasService;
        this.webClient = WebClient.builder().baseUrl(urlAgregador).build();
    }

    public void generarEstadisticas() {
        categoriasService.actualizarCategorias(); //primero me actualizo la base de datos de mis categorias
        this.generarEstadisticasColeccion();
        this.generarEstadisticasHecho();
        this.generarEstadisticasSolicitud();
    }

    public void eliminarEstadisticasAntiguas() {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(cantDiasPersistenciaEstaditicas);
        //TODO eliminar hechos anteriores a esta fecha
    }

    // --- Metodos Obtencion Controller --- //

    public List<E_HoraOcurrenciaPorCategoria> obtenerEstadisticasHoraPorCategoria(Categoria categoria) {
        return null;
    }

    // --- Metodos Privados --- //

    private void generarEstadisticasColeccion() {
        List<Coleccion> colecciones = this.obtenerColeccionesAgregador();
        GeneradorEstadisticas generador  = GeneradorEstadisticas.getInstance();

        // -- Estadistica provincia con mas hechos por coleccion -- //
        List<E_MayorProvinciaPorColeccion> e_provinciaPorColeccion = colecciones.stream()
                .map(c -> generador.mayorProvinciaPorColeccion(c, c.getHechos()))
                .toList();
        //TODO saveAll e_provinciaColeccion
    }

    private void generarEstadisticasHecho() {
        List<Hecho> hechos = this.obtenerHechosAgregador();
        Map< Categoria, List<Hecho> > hechosPorCategoria = hechos.stream().collect(Collectors.groupingBy(Hecho::getCategoria));
        GeneradorEstadisticas generador  = GeneradorEstadisticas.getInstance();

        // -- Estadistica provincia con mas hechos por categoria -- //
        List<E_MayorProvinciaPorCategoria> e_provinciaPorCategoria = hechosPorCategoria.entrySet().stream()
                .map(e -> generador.mayorProvinciaPorCategoria(e.getKey(), e.getValue()))
                .toList();
        //TODO saveAll List

        // -- Estadistica hora de ocurrencia del dia con mas hechos por categoria -- //
        List<E_HoraOcurrenciaPorCategoria> e_horaPorCategorias = hechosPorCategoria.entrySet().stream()
                .map(e -> generador.horaDiaPorCategoria(e.getKey(), e.getValue()))
                .toList();
        //TODO saveAll List

        // -- Estadistica categoria con mas hechos -- //
        E_MayorCategoria e_mayorCategoria = generador.mayorCategoria(hechosPorCategoria);
        //TODO save
    }

    private void generarEstadisticasSolicitud() {
        List<SolicitudEliminacion> solicitudes = this.obtenerSolicitudesEliminacionAgregador();
        GeneradorEstadisticas generador  = GeneradorEstadisticas.getInstance();

        // -- Estadistica cantidad de solicitudes spam -- //
        E_SolicitudesSpam e_solicitudesSpam = generador.solicitudesSpam(solicitudes);
        //TODO save
    }

    private List<Coleccion> obtenerColeccionesAgregador (){
        //TODO IMPORTANTE, EL AGREGADOR DEBE DEVOLVER LAS COLECCIONES CON SUS HECHOS EN AL MENOS UN ENDPOINT (por ejemplo usar el /privada)
        List<ColeccionInputDTO> coleccionesInputDTO =  webClient.get()
                .uri("/api/colecciones/privada")
                .retrieve()
                .bodyToFlux(ColeccionInputDTO.class)
                .collectList()
                .block();
        if (coleccionesInputDTO == null) {return null;}
        return coleccionesInputDTO.stream().map(DTOconverter::coleccionInputDTO).toList();
    }

    private List<Hecho> obtenerHechosAgregador (){
        List<HechoInputDTO> hechosInputDTO = webClient.get()
                .uri("/api/hechos/publica")
                .retrieve()
                .bodyToFlux(HechoInputDTO.class)
                .collectList()
                .block();
        if (hechosInputDTO == null) {return null;}
        return hechosInputDTO.stream().map(DTOconverter::hechoInputDTO).toList();
    }

    private List<SolicitudEliminacion> obtenerSolicitudesEliminacionAgregador (){
        List<SolicitudEliminacionInputDTO> solicitudesInputDTO = webClient.get()
                .uri("/api/solicitudes-eliminacion/publica")
                .retrieve()
                .bodyToFlux(SolicitudEliminacionInputDTO.class)
                .collectList()
                .block();
        if (solicitudesInputDTO == null) {return null;}
        return solicitudesInputDTO.stream().map(DTOconverter::solicitudEliminacionInputDTO).toList();
    }

}
