package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.HechoOutputDTO;
import org.springframework.http.ResponseEntity;

public interface IFuenteDinamicaService {
    ResponseEntity<Void> crearHecho(HechoOutputDTO hechoOutputDTO);
}
