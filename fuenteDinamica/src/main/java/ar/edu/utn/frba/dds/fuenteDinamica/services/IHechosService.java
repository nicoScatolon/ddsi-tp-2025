package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosService {
    void cargarHecho(HechoInputDTO hechoInputDTO);
    Hecho modificarHecho(HechoInputDTO hechoInputDTO);
    Hecho revisarHecho(Long idHecho, Long idAdmin, EstadoHecho nuevoEstado, String sugerencia);
    List<HechoOutputDTO> getHechos(LocalDateTime fechaDeCarga);
}
