package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.FuenteProxyInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechosFilterDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechoProxyOutputDTO;

import java.util.List;

public interface IFuenteProxyService {
    void agregarFuenteMetamapa(FuenteProxyInputDTO dto);
    void agregarFuenteDDS(FuenteProxyInputDTO dto);
    void eliminarFuente(String nombreFuente);
    List<HechoProxyOutputDTO> obtenerHechos();
    HechoProxyOutputDTO buscarHechoPorId(Long id, String nombreFuente);
    List<HechoProxyOutputDTO> filtrarHechos(HechosFilterDTO filtros);
    List<ColeccionInputDTO> obtenerColecciones();
    List<HechoProxyOutputDTO> hechosDeColeccion(String idColeccion);
    void crearSolicitudEliminacion(SolicitudEliminarHechoInputDTO solicitud);

}
