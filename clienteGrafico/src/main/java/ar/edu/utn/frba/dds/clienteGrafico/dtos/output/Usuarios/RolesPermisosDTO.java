package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Usuarios;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolesPermisosDTO {
    private Long userId;
    private String username;
    private Rol rol;
    private List<Permiso> permisos;
}
