package ar.edu.utn.frba.dds.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    private String codigoCategoria;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "fechaActualizacion",nullable = false)
    private LocalDateTime fechaActualizacion; //para la base de datos
}
