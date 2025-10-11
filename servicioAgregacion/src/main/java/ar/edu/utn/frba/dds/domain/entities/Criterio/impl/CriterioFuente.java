package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;

import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
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
@DiscriminatorValue("Fuente")
@NoArgsConstructor
public class CriterioFuente extends Criterio {

    @ManyToOne
    @JoinColumn(name = "fuente_id", nullable = true)
    @Getter
    @Setter
    private Fuente fuente;

    public CriterioFuente(Fuente fuente) {
        this.fuente = fuente;
    }

    @Override
    public Boolean pertenece(Hecho hecho){
        return Objects.equals(hecho.getFuente().getId(), this.fuente.getId());
    }

}
