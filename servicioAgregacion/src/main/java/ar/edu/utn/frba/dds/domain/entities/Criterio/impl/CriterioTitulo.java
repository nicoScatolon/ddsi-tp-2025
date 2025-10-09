package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@DiscriminatorValue("Titulo")
@Entity
public class CriterioTitulo extends Criterio {
    @Column(name = "titulo", nullable = true)
    private  String titulo;

    public CriterioTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public Boolean pertenece(Hecho hecho){
        return hecho.getTitulo().equals(this.titulo);
    }

}
