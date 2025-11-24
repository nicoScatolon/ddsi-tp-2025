package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.FuenteAMostrarOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.TipoFuenteProxy;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFuentesService {
    Fuente buscarPorId(Long id);
    List<Fuente> obtenerPorTipo(TipoFuenteProxy tipo);
    List<Fuente> obtenerTodas();
    List<FuenteAMostrarOutputDTO> getFuentes();
    void agregarFuenteDDS(FuenteInputDTO dto);
    void  agregarFuenteMetamapa(FuenteInputDTO dto);
    void eliminarFuente(String nombre);
}
