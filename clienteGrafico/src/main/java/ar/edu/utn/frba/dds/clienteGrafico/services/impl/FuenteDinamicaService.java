package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.EstadoHecho;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoDinamicaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.RevisionHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechoDinamicaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FuenteDinamicaService implements IFuenteDinamicaService {
    private final WebApiCallerService webApiCallerService;
    private final String fuenteDinamicaUrl;

    public FuenteDinamicaService(WebApiCallerService webApiCallerService,
                                 @Value("${fuente.dinamica.url}") String fuenteDinamicaUrl) {
        this.webApiCallerService = webApiCallerService;
        this.fuenteDinamicaUrl = fuenteDinamicaUrl;
    }

    @Override
    public ResponseEntity<Void> crearHecho(HechoDinamicaOutputDTO hechoDinamicaOutputDTO) {
        try {
            webApiCallerService.postPublic(
                    fuenteDinamicaUrl + "/api/fuenteDinamica/hechos",
                    hechoDinamicaOutputDTO,
                    Void.class
            );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al crear el hecho dinámico: " + e.getMessage(), e);
        }
    }

    @Override
    public List<HechoDinamicaInputDTO> obtenerHechosDinamica(EstadoHecho estadoHecho) {
        String url = fuenteDinamicaUrl + "/api/fuenteDinamica/hechos?estado=" + estadoHecho;
        try {
            return webApiCallerService.getPublicList(url, HechoDinamicaInputDTO.class);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener los hechos desde dinamica: " + e.getMessage(), e);
        }
    }

    @Override
    public HechoDinamicaInputDTO obtenerHechoDinamicaId(Long idHecho) {
        String url = fuenteDinamicaUrl + "/api/fuenteDinamica/hechos/" + idHecho;
        try {
            return webApiCallerService.get(url, HechoDinamicaInputDTO.class);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener el hecho desde dinamica: " + e.getMessage(), e);
        }
    }

    @Override
    public void enviarRevisionHechoDinamica(RevisionHechoInputDTO revisionHecho, Long adminId) {
        String url = fuenteDinamicaUrl + "/api/fuenteDinamica/hechos/admin/" + adminId;
        try {
            webApiCallerService.post(url, revisionHecho , Void.class);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener el hecho mapa: " + e.getMessage(), e);
        }
    }

    @Override
    public List<HechoDinamicaInputDTO> obtenerHechosDinamicaUsuario(Long usuarioId, EstadoHecho estado, Integer page) {
        StringBuilder urlBuilder = new StringBuilder(fuenteDinamicaUrl)
                .append("/api/fuenteDinamica/hechos/user/")
                .append(usuarioId)
                .append("?page=").append(page);

        // Agregar estado si existe
        if (estado != null) {
            urlBuilder.append("&estado=").append(estado);
        }

        String url = urlBuilder.toString();

        try {
            return webApiCallerService.getList(url, HechoDinamicaInputDTO.class);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener los hechos del usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<Void> editarHecho(HechoDinamicaOutputDTO hechoDTO) {
        try {
            webApiCallerService.put(
                    fuenteDinamicaUrl + "/api/fuenteDinamica/hechos/" + hechoDTO.getId(),
                    hechoDTO,
                    Void.class
            );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al editar el hecho " + hechoDTO.getId() + ": " + e.getMessage(), e);
        }
    }

}
