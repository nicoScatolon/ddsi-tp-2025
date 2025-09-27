package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.TipoFuenteProxy;

import java.util.List;

public interface IFuenteService {
    Fuente buscarPorId(Long id);
    List<Fuente> obtenerPorTipo(TipoFuenteProxy tipo);
    List<Fuente> obtenerTodas();
    void agregarFuenteDDS(FuenteInputDTO dto);
    void  agregarFuenteMetamapa(FuenteInputDTO dto);
    void eliminarFuente(String nombre);
}
