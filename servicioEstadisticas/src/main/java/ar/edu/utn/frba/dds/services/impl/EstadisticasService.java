package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOconverter;
import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.estadisticas.*;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Estadisticas.*;
import ar.edu.utn.frba.dds.domain.entities.Hecho;
import ar.edu.utn.frba.dds.domain.entities.SolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.repository.*;
import ar.edu.utn.frba.dds.services.ICategoriasService;
import ar.edu.utn.frba.dds.services.IEstadisticasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EstadisticasService implements IEstadisticasService {
    private final ICategoriasService categoriasService;

    private final IE_MayorCategoriaRepository mayorCategoriaRepository;
    private final IE_SolicitudesSpamRepository solicitudesSpamRepository;
    private final IE_HoraOcuPorCategoriaRepository horaOcuPorCategoriaRepository;
    private final IE_MayorProvinciaPorCategoriaRepository mayorProvinciaPorCategoriaRepository;
    private final IE_MayorProvinciaPorColeccionRepository mayorProvinciaPorColeccionRepository;

    private final WebClient webClient;
    @Value("${estadisticas.cantDiasPersistencia}") Integer cantDiasPersistenciaEstaditicas;

    public EstadisticasService(ICategoriasService categoriasService,
                               IE_MayorCategoriaRepository mayorCategoriaRepository,
                               IE_SolicitudesSpamRepository solicitudesSpamRepository,
                               IE_HoraOcuPorCategoriaRepository horaOcuPorCategoriaRepository,
                               IE_MayorProvinciaPorCategoriaRepository mayorProvinciaPorCategoriaRepository,
                               IE_MayorProvinciaPorColeccionRepository mayorProvinciaPorColeccionRepository,
                               @Value("${agregador.base-url}") String urlAgregador)

        {
        this.categoriasService = categoriasService;
        this.mayorCategoriaRepository = mayorCategoriaRepository;
        this.solicitudesSpamRepository = solicitudesSpamRepository;
        this.horaOcuPorCategoriaRepository = horaOcuPorCategoriaRepository;
        this.mayorProvinciaPorCategoriaRepository  = mayorProvinciaPorCategoriaRepository;
        this.mayorProvinciaPorColeccionRepository = mayorProvinciaPorColeccionRepository;

        this.webClient = WebClient.builder().baseUrl(urlAgregador).build();
    }

    public void generarEstadisticas() {
        log.info("[ESTADISTICAS] Inicio de generación");
        try {
            categoriasService.actualizarCategorias(); //primero me actualizo la base de datos de mis categorias
        } catch (Exception e) {
            log.error("Error al actualizar las categorias" + e.getMessage());
            return;
        }
        try {
            this.generarEstadisticasColeccion();
        } catch (Exception e) {
            log.error("Error al generar las Estadisticas de las Colecciones" + e.getMessage());
        }

        try {
            this.generarEstadisticasHecho();
        } catch (Exception e) {
            log.error("Error al generar las Estadisticas de los Hechos" + e.getMessage());
        }

        try {
            this.generarEstadisticasSolicitud();
        } catch (Exception e) {
            log.error("Error al generar las Estadisticas de las Solicitudes de Eliminacion" + e.getMessage());
        }
        
        log.info("[ESTADISTICAS] Completadas");
    }

    public void eliminarEstadisticasAntiguas() {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(cantDiasPersistenciaEstaditicas);
        this.solicitudesSpamRepository.deleteByFechaDeCalculoBefore(fechaLimite);
        this.mayorCategoriaRepository.deleteByFechaDeCalculoBefore(fechaLimite);
        this.horaOcuPorCategoriaRepository.deleteByFechaDeCalculoBefore(fechaLimite);
        this.mayorProvinciaPorCategoriaRepository.deleteByFechaDeCalculoBefore(fechaLimite);
        this.mayorProvinciaPorColeccionRepository.deleteByFechaDeCalculoBefore(fechaLimite);
    }


    // --- Metodos Obtencion Controller --- //


    public List<E_MayorCategoriaOutputDTO> obtenerEstadisticasMayorCategoria() {
        return this.mayorCategoriaRepository.findAll().stream().map(DTOconverter::eMayorCategoriaOutputDTO).toList();
    }

    public List<E_SolicitudesSpamOutputDTO> obtenerEstadisticasSolicitudesSpam() {
        return this.solicitudesSpamRepository.findAll().stream().map(DTOconverter::eSolicitudesSpamOutputDTO).toList();
    }

    public List<E_MayorProvPorCategoriaOutputDTO> obtenerEstadisticasMayorProvPorCategoria(String idCategoria) {
        if ( idCategoria == null) {
            return this.mayorProvinciaPorCategoriaRepository.findAll().stream()
                    .map(DTOconverter::eMayorProvinciaPorCategoriaOutputDTO)
                    .toList();
        }
        Categoria categoria = this.categoriasService.findById(idCategoria);
        if (categoria == null) {return null;} // TODO deberia ser un response entity

        return this.mayorProvinciaPorCategoriaRepository.findByCategoria(categoria).stream()
                .map(DTOconverter::eMayorProvinciaPorCategoriaOutputDTO)
                .toList();
    }

    public List<E_HoraOcuPorCategoriaOutputDTO> obtenerEstadisticasHoraPorCategoria(String idCategoria) {
        if ( idCategoria == null ) {
            return this.horaOcuPorCategoriaRepository.findAll().stream()
                    .map(DTOconverter::eHoraOcuPorCategoriaOutputDTO)
                    .toList();
        }
        Categoria categoria = this.categoriasService.findById(idCategoria);
        if (categoria == null) {return null;} // TODO deberia ser un response entity

        return this.horaOcuPorCategoriaRepository.findByCategoria(categoria).stream()
                .map(DTOconverter::eHoraOcuPorCategoriaOutputDTO)
                .toList();
    }

    public List<E_MayorProvPorColeccionOutputDTO> obtenerEstadisticasMayorProvPorColeccion(String handleColeccion) {
        if ( handleColeccion == null) {
            return this.mayorProvinciaPorColeccionRepository.findAll().stream()
                    .map(DTOconverter::eMayorProvinciaPorColeccionOutputDTO)
                    .toList();
        }

        return this.mayorProvinciaPorColeccionRepository.findByColeccion_Handle(handleColeccion).stream()
                .map(DTOconverter::eMayorProvinciaPorColeccionOutputDTO)
                .toList();
    }

    // --- Metodos Privados --- //


    private void generarEstadisticasColeccion() {
        List<Coleccion> colecciones = this.obtenerColeccionesAgregador();
        if (colecciones == null || colecciones.isEmpty()) {
            throw new RuntimeException("No se recibieron correctamente las colecciones");
        }
        GeneradorEstadisticas generador  = GeneradorEstadisticas.getInstance();

        // -- Estadistica provincia con mas hechos por coleccion -- //
        List<E_MayorProvinciaPorColeccion> e_provinciaPorColeccion = colecciones.stream()
                .map(c -> generador.mayorProvinciaPorColeccion(c, c.getHechos()))
                .filter(Objects::nonNull)
                .toList();
        this.mayorProvinciaPorColeccionRepository.saveAll(e_provinciaPorColeccion);
    }


    private void generarEstadisticasHecho() {
        List<Hecho> hechos = this.obtenerHechosAgregador();
        if (hechos == null || hechos.isEmpty()) {
            throw new RuntimeException("No se recibieron correctamente las solicitudes");
        }

        Map< Categoria, List<Hecho> > hechosPorCategoria = hechos.stream().collect(Collectors.groupingBy(Hecho::getCategoria));
        GeneradorEstadisticas generador  = GeneradorEstadisticas.getInstance();

        // -- Estadistica provincia con mas hechos por categoria -- //
        List<E_MayorProvinciaPorCategoria> e_provinciaPorCategoria = hechosPorCategoria.entrySet().stream()
                .map(e -> generador.mayorProvinciaPorCategoria(e.getKey(), e.getValue()))
                .filter(Objects::nonNull)
                .toList();
        this.mayorProvinciaPorCategoriaRepository.saveAll(e_provinciaPorCategoria);

        // -- Estadistica hora de ocurrencia del dia con mas hechos por categoria -- //
        List<E_HoraOcurrenciaPorCategoria> e_horaPorCategorias = hechosPorCategoria.entrySet().stream()
                .map(e -> generador.horaDiaPorCategoria(e.getKey(), e.getValue()))
                .filter(Objects::nonNull)
                .toList();
        this.horaOcuPorCategoriaRepository.saveAll(e_horaPorCategorias);

        // -- Estadistica categoria con mas hechos -- //
        E_MayorCategoria e_mayorCategoria = generador.mayorCategoria(hechosPorCategoria);
        this.mayorCategoriaRepository.save(e_mayorCategoria);
    }

    private void generarEstadisticasSolicitud() {
        List<SolicitudEliminacion> solicitudes = this.obtenerSolicitudesEliminacionAgregador();

        GeneradorEstadisticas generador  = GeneradorEstadisticas.getInstance();

        // -- Estadistica cantidad de solicitudes spam -- //
        E_SolicitudesSpam e_solicitudesSpam = generador.solicitudesSpam(solicitudes);
        this.solicitudesSpamRepository.save(e_solicitudesSpam);
    }

    private List<Coleccion> obtenerColeccionesAgregador (){
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

    // TEST


    @Override
    public void generarEstadisticasTest(List<HechoInputDTO> hechosDTO){
        List<Hecho> hechos = hechosDTO.stream().map(DTOconverter::hechoInputDTO).toList();
        if (hechos == null || hechos.isEmpty()) {
            throw new RuntimeException("No se recibieron correctamente las solicitudes");
        }

        Map< Categoria, List<Hecho> > hechosPorCategoria = hechos.stream().collect(Collectors.groupingBy(Hecho::getCategoria));
        GeneradorEstadisticas generador  = GeneradorEstadisticas.getInstance();

        categoriasService.actualizarCategoriasTest( hechosPorCategoria.keySet().stream().toList() );

        List<E_MayorProvinciaPorCategoria> e_provinciaPorCategoria = hechosPorCategoria.entrySet().stream()
                .map(e -> generador.mayorProvinciaPorCategoria(e.getKey(), e.getValue()))
                .toList();
        this.mayorProvinciaPorCategoriaRepository.saveAll(e_provinciaPorCategoria);

        // -- Estadistica hora de ocurrencia del dia con mas hechos por categoria -- //
        List<E_HoraOcurrenciaPorCategoria> e_horaPorCategorias = hechosPorCategoria.entrySet().stream()
                .map(e -> generador.horaDiaPorCategoria(e.getKey(), e.getValue()))
                .toList();
        this.horaOcuPorCategoriaRepository.saveAll(e_horaPorCategorias);

        // -- Estadistica categoria con mas hechos -- //
        E_MayorCategoria e_mayorCategoria = generador.mayorCategoria(hechosPorCategoria);
        this.mayorCategoriaRepository.save(e_mayorCategoria);
    }
}
