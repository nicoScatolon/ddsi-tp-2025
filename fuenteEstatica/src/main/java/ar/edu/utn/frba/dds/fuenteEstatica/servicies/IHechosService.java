package ar.edu.utn.frba.dds.fuenteEstatica.servicies;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Hecho;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosService {
    List <Hecho> importarArchivoHechos(String path);
    List<HechoOutputDTO> getAllHechosPorFecha(LocalDateTime fechaDeCarga);


}
