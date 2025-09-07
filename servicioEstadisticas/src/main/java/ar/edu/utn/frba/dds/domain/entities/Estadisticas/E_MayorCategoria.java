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
@Table(name = "MayorCategoria")
public class E_MayorCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria; //la condicion del calculo

    @Column(name = "cant-hechos-categoria")
    private Integer cantHechosCategoria;

    @Column(name = "cant-hechos-totales")
    private Integer cantHechosTotales;

    @Column(name = "fecha-calculo")
    private LocalDateTime fechaDeCalculo;
}
