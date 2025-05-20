package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuentes.Fuente;

import java.util.List;

public interface IAgregadorDeHechosService {
    public List<HechoInputDTO> recolectarHechos(List<Fuente> fuentes);
}
