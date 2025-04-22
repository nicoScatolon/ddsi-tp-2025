package ar.edu.utn.frba.dds.Criterio;

import ar.edu.utn.frba.dds.Hechos.Categoria;
import ar.edu.utn.frba.dds.Hechos.Hecho;

public class CriterioCategoria implements CriterioInterfaz {
    private Categoria categoria;

    public CriterioCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public Boolean pertenece(Hecho hecho){
        return hecho.getCategoria()==this.categoria;
    }
}
