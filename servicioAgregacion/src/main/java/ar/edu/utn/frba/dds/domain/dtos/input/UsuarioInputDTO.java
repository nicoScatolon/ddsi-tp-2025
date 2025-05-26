package ar.edu.utn.frba.dds.domain.dtos.input;

import lombok.Data;

@Data
public class UsuarioInputDTO {
    private Long id;

    private String nombre;
    private String apellido;
}
