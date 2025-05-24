

package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;


import ar.edu.utn.frba.dds.domain.entities.Criterio.CriterioInterfaz;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;

public class CriterioTitulo implements CriterioInterfaz {
    private String titulo;

    public CriterioTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public Boolean pertenece(IHecho hecho){
        return hecho.getTitulo().equals(this.titulo);
    }

}
