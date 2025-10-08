package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;

import ar.edu.utn.frba.dds.domain.entities.Etiqueta;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@DiscriminatorValue("Etiqueta")
@NoArgsConstructor
public class CriterioEtiqueta extends Criterio {

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @Getter
    @Setter
    private Etiqueta etiqueta;

    public CriterioEtiqueta(Etiqueta etiqueta) {this.etiqueta = etiqueta;}

    @Override
    public Boolean pertenece(Hecho hecho){
        Long idEtiqueta = etiqueta.getId();
        return !hecho.getEtiquetas().stream().map(e -> Objects.equals(e.getId(), idEtiqueta)).toList().isEmpty();
    }

}
