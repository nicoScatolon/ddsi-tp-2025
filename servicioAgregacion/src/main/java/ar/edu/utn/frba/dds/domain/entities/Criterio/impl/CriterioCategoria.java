package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;


import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Criterio.CriterioInterfaz;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;

public class CriterioCategoria implements CriterioInterfaz {
    private final Categoria categoria;

    public CriterioCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public Boolean pertenece(HechoBase hecho){
        return hecho.getCategoria()==this.categoria;
    }
}
