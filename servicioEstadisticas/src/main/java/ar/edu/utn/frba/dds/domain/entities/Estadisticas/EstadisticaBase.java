package ar.edu.utn.frba.dds.domain.entities.Estadisticas;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Getter
@Setter

@MappedSuperclass
public abstract class EstadisticaBase {
    @Column(name = "fecha-calculo", nullable = false)
    protected LocalDateTime fechaDeCalculo;
}
