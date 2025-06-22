package ar.edu.utn.frba.dds.fuenteEstatica.servicies;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output.HechoOutputDTO;

import java.util.List;

public interface IHechosService {
    List <HechoOutputDTO> getAllHechosParaActualizar();
    List<HechoOutputDTO> importarArchivoHechos(String path);


}
