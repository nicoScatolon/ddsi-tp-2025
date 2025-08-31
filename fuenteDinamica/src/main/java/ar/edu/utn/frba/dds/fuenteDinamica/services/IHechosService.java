package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.ModificarHechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosService {
    void cargarHecho(ModificarHechoInputDTO inputDTO);
    void modificarHecho(ModificarHechoInputDTO  modificarHechoInputDTO);
    Hecho revisarHecho(Long idHecho, Long idAdmin, EstadoHecho nuevoEstado, String sugerencia);
    List<HechoOutputDTO> getHechos(LocalDateTime fechaDeCarga);
}
