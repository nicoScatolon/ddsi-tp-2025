package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.UsuarioInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Usuarios.*;

import java.util.List;

public interface IGestionUsuariosService {
    AuthResponseDTO login(String username, String password);
    RolesPermisosDTO getRolesPermisos(String accessToken);
    List<UsuarioResponseDTO> obtenerTodosLosUsuarios();
    UsuarioInputDTO obtenerUsuarioPorId(Long id);
    UsuarioResponseDTO crearUsuario(RegisterUsuarioRequestDTO usuarioDTO);
    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioOutputDTO usuarioDTO); //todo revisar
    void eliminarUsuario(Long id);
    Boolean existeUsuario(Long id);
    UsuarioResponseDTO crearAdmin(RegisterUsuarioRequestDTO usuario);
}
