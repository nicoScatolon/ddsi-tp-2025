package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.EstadoHecho;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoDinamicaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.RevisionHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechoDinamicaOutputDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IFuenteDinamicaService {
    ResponseEntity<Void> crearHecho(HechoDinamicaOutputDTO hechoDinamicaOutputDTO);
    List<HechoDinamicaInputDTO> obtenerHechosDinamica(EstadoHecho estadoHecho);
    void enviarRevisionHechoDinamica(RevisionHechoInputDTO revisionHecho, Long adminId);
    HechoDinamicaInputDTO obtenerHechoDinamicaId(Long idHecho);
    List<HechoDinamicaInputDTO> obtenerHechosDinamicaUsuario(Long usuarioId, EstadoHecho estado, Integer page);
    ResponseEntity<Void> editarHecho(HechoDinamicaOutputDTO hechoDTO);
}
