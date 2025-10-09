package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;


import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
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
@DiscriminatorValue("Categoria")
@NoArgsConstructor

public class CriterioCategoria extends Criterio {
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = true)
    @Getter
    @Setter
    private Categoria categoria;

    public CriterioCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public Boolean pertenece(Hecho hecho){
        return Objects.equals(hecho.getCategoria().getCodigoCategoria(), this.categoria.getCodigoCategoria());
    }
}
