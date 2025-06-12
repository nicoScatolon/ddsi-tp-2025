package ar.edu.utn.frba.dds.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteDinamica;
import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteEstatica;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FuenteDinamicaAdapter implements FuenteAdapter {
    private FuenteDinamica fuenteDinamica;

    @Override
    public void setFuente(IFuente fuente) {
        if (! (fuente instanceof FuenteDinamica) ) {throw new RuntimeException("Fuente no valida");}
        else fuenteDinamica = (FuenteDinamica) fuente;
    }

    @Override
    public List<IHechoInputDTO> obtenerHechosFuente() {
        return fuenteDinamica.getHechos().stream().map(dto -> (IHechoInputDTO) dto).toList();
    }
}
