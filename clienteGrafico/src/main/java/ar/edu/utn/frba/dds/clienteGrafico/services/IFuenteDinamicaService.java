package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoInputDTO;
import org.springframework.http.ResponseEntity;

public interface IFuenteDinamicaService {
    ResponseEntity<Void> crearHecho(HechoInputDTO hechoInputDTO);
}
