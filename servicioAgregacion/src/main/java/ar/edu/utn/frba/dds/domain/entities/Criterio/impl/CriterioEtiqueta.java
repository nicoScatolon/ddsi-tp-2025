package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;

import ar.edu.utn.frba.dds.domain.entities.Etiqueta;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;


@Entity
@DiscriminatorValue("Etiqueta")
@NoArgsConstructor
public class CriterioEtiqueta extends Criterio {

    @ManyToMany
    @JoinTable(
            name = "criterio_etiqueta_etiquetas",
            joinColumns = @JoinColumn(name = "criterio_id"),
            inverseJoinColumns = @JoinColumn(name = "etiqueta_id")
    )
    @Getter
    @Setter
    private List<Etiqueta> etiquetas; // Cambié el nombre de 'etiqueta' a 'etiquetas'

    public CriterioEtiqueta(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    @Override
    public Boolean pertenece(Hecho hecho){
        // Verificar si el hecho tiene alguna de las etiquetas del criterio
        return hecho.getEtiquetas().stream()
                .anyMatch(etiquetaHecho ->
                        etiquetas.stream()
                                .anyMatch(etiquetaCriterio ->
                                        etiquetaCriterio.getId().equals(etiquetaHecho.getId())
                                )
                );
    }
}