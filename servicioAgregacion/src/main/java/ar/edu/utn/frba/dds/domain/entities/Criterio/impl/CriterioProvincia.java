package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@Entity
@DiscriminatorValue("Provincia")
public class CriterioProvincia extends Criterio {

    @Column(name="provincia", nullable = false)
    private String provincia;

    public String getProvincia() {
        return provincia;
    }

    public CriterioProvincia(String provincia) {
        this.provincia = provincia;
    }

    @Override
    public Boolean pertenece(Hecho hecho){
        Ubicacion nuevaUbicacion = hecho.getUbicacion();
        return Objects.equals( nuevaUbicacion.getProvincia(), provincia );
    }

}
