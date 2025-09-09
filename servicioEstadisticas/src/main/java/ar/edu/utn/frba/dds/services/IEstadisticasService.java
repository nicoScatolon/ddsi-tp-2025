package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.DTOconverter;
import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.estadisticas.*;
import ar.edu.utn.frba.dds.domain.entities.Categoria;

import java.time.LocalDateTime;
import java.util.List;

public interface IEstadisticasService {
    void generarEstadisticas();
    void eliminarEstadisticasAntiguas();
    List<E_HoraOcuPorCategoriaOutputDTO> obtenerEstadisticasHoraPorCategoria(CategoriaInputDTO categoriaDTO);
    List<E_MayorCategoriaOutputDTO> obtenerEstadisticasMayorCategoria();
    List<E_SolicitudesSpamOutputDTO> obtenerEstadisticasSolicitudesSpam();
    List<E_MayorProvPorCategoriaOutputDTO> obtenerEstadisticasMayorProvPorCategoria(CategoriaInputDTO categoriaDTO);
    List<E_MayorProvPorColeccionOutputDTO> obtenerEstadisticasMayorProvPorColeccion(ColeccionInputDTO colInputDTO);

    //test
    void generarEstadisticasTest();
}
