package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.TipoFuenteProxy;

import java.util.List;

public interface IFuenteService {
    IFuente buscarPorId(Long id);
    List<IFuente> obtenerPorTipo(TipoFuenteProxy tipo);
    List<IFuente> obtenerTodas();
    FuenteOutputDTO agregarFuenteDDS(FuenteInputDTO dto);
    FuenteOutputDTO agregarFuenteMetamapa(FuenteInputDTO dto);
    void eliminarFuente(String nombre);
}
