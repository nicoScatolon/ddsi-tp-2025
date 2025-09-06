package ar.edu.utn.frba.dds.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Hecho {
    private Long id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private List<Etiqueta> etiquetas; //posible DTO
    private List<ContenidoMultimedia> contenidoMultimedia; //posible DTO
    private LocalDate fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;
    private Contribuyente contribuyente; //posible DTO
    private Boolean cargadoAninimamente;
    private Fuente fuente;
}
