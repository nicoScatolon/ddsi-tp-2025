package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.FuenteProxyInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechosFilterDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechoProxyOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteProxyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FuenteProxyService implements IFuenteProxyService {
    private final WebApiCallerService webApiCallerService;
    private final String fuenteProxyUrl;

    public FuenteProxyService(WebApiCallerService webApiCallerService,
                                 @Value("http://localhost:8083") String fuenteProxyUrl) {
        this.webApiCallerService = webApiCallerService;
        this.fuenteProxyUrl = fuenteProxyUrl;
    }


    @Override
    public void agregarFuenteMetamapa(FuenteProxyInputDTO dto) {
        String url = fuenteProxyUrl + "/api/fuenteProxy/metamapa";
        try {
            webApiCallerService.post(url, dto, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al agregar fuente MetaMapa: " + e.getMessage(), e);
        }
    }

    @Override
    public void agregarFuenteDDS(FuenteProxyInputDTO dto) {
        String url = fuenteProxyUrl + "/api/fuenteProxy/externa/dds";
        try {
            webApiCallerService.post(url, dto, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al agregar fuente DDS: " + e.getMessage(), e);
        }
    }


    @Override
    public void eliminarFuente(String nombreFuente) {
        String url = fuenteProxyUrl + "/api/fuenteProxy/" + nombreFuente;
        try {
            webApiCallerService.delete(url);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar fuente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<HechoProxyOutputDTO> obtenerHechos() {
        try {
            String url = fuenteProxyUrl + "/api/fuenteProxy/hechos";
            return webApiCallerService.getList(url, HechoProxyOutputDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener hechos del proxy", e);
        }
    }

    @Override
    public HechoProxyOutputDTO buscarHechoPorId(Long id, String nombreFuente) {
        try {
            String url = fuenteProxyUrl + "/api/fuenteProxy/hechos/" + id + "?nombreFuente=" + nombreFuente;
            return webApiCallerService.get(url, HechoProxyOutputDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener hecho por ID", e);
        }
    }

    @Override
    public List<HechoProxyOutputDTO> filtrarHechos(HechosFilterDTO filtros) {
        try {
            StringBuilder url = new StringBuilder(fuenteProxyUrl + "/api/fuenteProxy/hechos/filtrados?");
            filtros.asMap().forEach((key, val) -> url.append(key).append("=").append(val).append("&"));
            return webApiCallerService.getList(url.toString(), HechoProxyOutputDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al filtrar hechos", e);
        }
    }


    @Override
    public List<ColeccionInputDTO> obtenerColecciones() {
        try {
            String url = fuenteProxyUrl + "/api/fuenteProxy/colecciones";
            return webApiCallerService.getList(url, ColeccionInputDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener colecciones", e);
        }
    }

    @Override
    public List<HechoProxyOutputDTO> hechosDeColeccion(String idColeccion) {
        try {
            String url = fuenteProxyUrl + "/api/fuenteProxy/colecciones/hechos?id_coleccion=" + idColeccion;
            return webApiCallerService.getList(url, HechoProxyOutputDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener hechos de colección", e);
        }
    }

    @Override
    public void crearSolicitudEliminacion(SolicitudEliminarHechoInputDTO solicitud) {
        try {
            String url = fuenteProxyUrl + "/api/fuenteProxy/solicitudes/solicitud";
            webApiCallerService.post(url, solicitud, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear solicitud de eliminación", e);
        }
    }




}
