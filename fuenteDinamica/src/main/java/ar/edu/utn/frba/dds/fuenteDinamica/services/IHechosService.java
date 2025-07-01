package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.ContribuyenteInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;

public interface IHechosService {
    Hecho cargarHecho(HechoInputDTO hechoDTO, ContribuyenteInputDTO contribuyenteDTO, ICategoriaService categoriaService);
    Hecho modificarHecho(HechoInputDTO hechoDTO, ContribuyenteInputDTO contribuyenteDTO);
    Hecho revisarHecho(Long idHecho, Long idAdmin, EstadoHecho nuevoEstado, String sugerencia);
}
