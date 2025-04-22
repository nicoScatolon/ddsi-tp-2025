

package ar.edu.utn.frba.dds.Criterio;

import ar.edu.utn.frba.dds.Criterio.CriterioInterfaz;
import ar.edu.utn.frba.dds.Hechos.Hecho;

public class CriterioTitulo implements CriterioInterfaz {
    private String titulo;

    public CriterioTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public Boolean pertenece(Hecho hecho){
        return hecho.getTitulo().equals(this.titulo);
    }

}
