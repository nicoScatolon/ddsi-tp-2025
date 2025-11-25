package ar.edu.utn.frba.dds.models.dtos;

import ar.edu.utn.frba.dds.models.entities.Usuario;

public final class UsuarioMapper {
    private UsuarioMapper() {}

    public static UsuarioResponseDTO toResponse(Usuario u) {
        return UsuarioResponseDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .nombre(u.getNombre())
                .apellido(u.getApellido())
                .email(u.getEmail())
                .rol(u.getRol())
                .fecha_nacimiento(u.getFechaNacimiento())
                .fecha_registro(u.getCreadoEn().toLocalDate())
                .ultima_actividad(u.getUltimoAcceso())
                .build();
    }
}
