package ar.edu.utn.frba.dds.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteDinamica;
import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteEstatica;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FuenteDinamicaAdapter implements FuenteAdapter {
    private FuenteDinamica fuenteDinamica;

    @Override
    public List<IHechoInputDTO> obtenerHechosFuente() {
        return fuenteDinamica.getHechos().stream().map(dto -> (IHechoInputDTO) dto).toList();
    }
}
