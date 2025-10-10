package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechoOutputDTO;
import org.springframework.http.ResponseEntity;

public interface IFuenteDinamicaService {
    ResponseEntity<Void> crearHecho(HechoOutputDTO hechoOutputDTO);

}
