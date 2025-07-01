package ar.edu.utn.frba.dds.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteDinamica;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FuenteDinamicaAdapter implements FuenteAdapter {
    private FuenteDinamica fuenteDinamica;

    @Override
    public void setFuente(IFuente fuente) {
        if (!(fuente instanceof FuenteDinamica)) {
            throw new RuntimeException("Fuente no valida");
        } else fuenteDinamica = (FuenteDinamica) fuente;
    }

    @Override
    public List<Hecho> actualizarHechos() {
        return fuenteDinamica.updateHechos();
    }

    @Override
    public List<Hecho> obtenerHechos() {
        return fuenteDinamica.getMapHechos().values().stream().toList();
    }

}
