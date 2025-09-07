package ar.edu.utn.frba.dds.domain.entities.Estadisticas;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "HoraOcurrenciaPorCategoria")
public class E_HoraOcurrenciaPorCategoria extends EstadisticaBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria; //la condicion del calculo

    @Column(name = "HoraDia")
    private Integer horaDia; //el resultado

    @Column(name = "cant-hechos-hora")
    private Integer cantHechosHora;

    @Column(name = "cant-hechos-totales")
    private Integer cantHechosTotales;
}
