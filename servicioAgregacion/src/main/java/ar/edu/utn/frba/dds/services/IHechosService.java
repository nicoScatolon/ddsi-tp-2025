package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechosPaginadosResponseDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> findAllOutput();
    List<Hecho> findAll();
    HechoOutputDTO findByID(Long id);
    Hecho findEntidadPorId(Long id);
    void actualizarHechosRepository(List<Hecho> hechosActualizados);
    List<HechoOutputDTO> getHechos(CategoriaInputDTO cat, LocalDateTime fReporteDesde, LocalDateTime fReporteHasta, LocalDate fAconDesde, LocalDate fAconHasta, UbicacionInputDTO ubicacion);
}