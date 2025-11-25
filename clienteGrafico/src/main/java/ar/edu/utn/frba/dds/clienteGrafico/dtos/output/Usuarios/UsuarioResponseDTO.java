package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Usuarios;

import lombok.*;
import java.time.Instant;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String username;

    private List<String> roles;    // ["CONTRIBUYENTE", "ADMIN"] lo que sea
    private Boolean activo;        // o estado

    private Instant creadoEn;
    private Instant actualizadoEn;
}
