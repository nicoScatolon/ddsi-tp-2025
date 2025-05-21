package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;


import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Criterio.CriterioInterfaz;
import ar.edu.utn.frba.dds.domain.entities.Hecho;

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
