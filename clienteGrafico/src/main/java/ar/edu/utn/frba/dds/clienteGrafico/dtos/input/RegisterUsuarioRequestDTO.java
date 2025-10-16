package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

// DTO para ALTA / REGISTRO (request hacia el servicio de auth)

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterUsuarioRequestDTO {
    private String nombre;

    private String apellido;

    private String email;

    private String username;

    private String password;

}
