package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.IContenidoMultimedia;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Ubicacion;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Usuario;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HechoInputDTO {
    private String titulo;
    private String descripcion;
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga; // puede venir como NULL si es la primera carga

    private IContenidoMultimedia contenidoMultimedia; //null

    private UsuarioInputDTO contribuyente; //TODO ver como lo recibo
}
