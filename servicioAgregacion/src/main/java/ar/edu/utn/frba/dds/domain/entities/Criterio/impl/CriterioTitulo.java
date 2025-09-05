

package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;


import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor

@Entity
public class CriterioTitulo extends Criterio {
    private  String titulo;

    public CriterioTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public Boolean pertenece(Hecho hecho){
        return hecho.getTitulo().equals(this.titulo);
    }

}
