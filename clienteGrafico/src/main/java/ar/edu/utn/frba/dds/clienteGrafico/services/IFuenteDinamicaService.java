package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.EstadoHecho;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoDinamicaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.RevisionHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechoOutputDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IFuenteDinamicaService {
    ResponseEntity<Void> crearHecho(HechoOutputDTO hechoOutputDTO);
    List<HechoDinamicaInputDTO> obtenerHechosDinamica(EstadoHecho estadoHecho);
    void enviarRevisionHechoDinamica(RevisionHechoInputDTO revisionHecho, Long adminId);
}
