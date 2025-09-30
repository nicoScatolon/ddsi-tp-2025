package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.TipoFuenteProxy;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFuentesService {
    Fuente buscarPorId(Long id);
    List<Fuente> obtenerPorTipo(TipoFuenteProxy tipo);
    List<Fuente> obtenerTodas();
    void agregarFuenteDDS(FuenteInputDTO dto);
    void  agregarFuenteMetamapa(FuenteInputDTO dto);
    void eliminarFuente(String nombre);

}
