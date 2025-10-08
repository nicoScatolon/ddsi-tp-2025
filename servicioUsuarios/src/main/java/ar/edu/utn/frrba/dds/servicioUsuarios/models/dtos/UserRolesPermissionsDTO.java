package ar.edu.utn.frrba.dds.servicioUsuarios.models.dtos;

import ar.edu.utn.frrba.dds.servicioUsuarios.models.entities.Permiso;
import ar.edu.utn.frrba.dds.servicioUsuarios.models.entities.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesPermissionsDTO {
    private String username;
    private Rol rol;
    private List<Permiso> permisos;
}