package ar.edu.utn.frba.dds.fuenteEstatica.servicies;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.ImportadorHechos;

import java.util.List;

public interface IHechosService {
    public List<Hecho> importarArchivoHechos(String path, ImportadorHechos importador);
    public List<HechoOutputDTO> getAllHechosParaActualizar();

}
