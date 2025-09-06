package ar.edu.utn.frba.dds.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    //private List<Etiqueta> etiquetas; //posible DTO
    //private List<ContenidoMultimedia> contenidoMultimedia; //posible DTO
    private LocalDateTime fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;
    //private Contribuyente contribuyente; //posible DTO
    //private Boolean cargadoAninimamente;
    //private Fuente fuente;

    // -- Los datos comentados no son necesarios para las estadisticas actuales, pero vienen en el input DTO
    // -- asi que podemos obtenerlos sin modificar la comunicacion, solo el proceso de creacion de la entidad
    // -- como no son necesarios actualmente no los cargo del DTO
}
