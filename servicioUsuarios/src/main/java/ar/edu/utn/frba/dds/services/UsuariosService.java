package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.models.dtos.RegistroUsuarioDTO;
import ar.edu.utn.frba.dds.models.dtos.UsuarioInputDTO;
import ar.edu.utn.frba.dds.models.entities.Permiso;
import ar.edu.utn.frba.dds.models.entities.Rol;
import ar.edu.utn.frba.dds.models.entities.Usuario;
import ar.edu.utn.frba.dds.models.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuariosService {
    private final UsuarioRepository usuarios;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario registrarUsuario(RegistroUsuarioDTO dto, Rol rol) {
        if (usuarios.existsByNombre(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        if (usuarios.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        LocalDate hoy = LocalDate.now();
        if (dto.getFecha_nacimiento() == null || !dto.getFecha_nacimiento().isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha de nacimiento no es válida");
        }

        Usuario nuevoUsuario = registroUsuarioDTO(dto);

        if (rol.equals(Rol.ADMIN)) {
            nuevoUsuario.setPermisos(Arrays.stream(Permiso.values())
                    .collect(Collectors.toCollection(ArrayList::new)));
        } else if (!rol.equals(Rol.CONTRIBUYENTE)) {
            throw new IllegalArgumentException("El rol ingresado no es válido");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        nuevoUsuario.setPassword(encoder.encode(dto.getPassword()));

        nuevoUsuario.setRol(rol);

        nuevoUsuario.setCreadoEn(LocalDateTime.now());

        return usuarios.save(nuevoUsuario);
    }

    public Usuario actualizarUsuario(Long id, UsuarioInputDTO dto) {

        Usuario usuario = this.obtenerPorId(id);

        if(!hayCambios(usuario,dto)){
            throw new RuntimeException("No hay cambios");
        }

        boolean cambioPassword = (dto.getCurrentPassword() != null && !dto.getCurrentPassword().isEmpty()) ||
                (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) ||
                (dto.getConfirmNewPassword() != null && !dto.getConfirmNewPassword().isEmpty());
        if(cambioPassword) {

            String current = dto.getCurrentPassword();
            String newPwd = dto.getNewPassword();
            String confirm = dto.getConfirmNewPassword();

            if(current == null || current.isEmpty())
                throw new RuntimeException("Debe ingresar la contraseña actual");

            if(newPwd == null || newPwd.isEmpty())
                throw new RuntimeException("Debe ingresar la nueva contraseña");

            if(confirm == null || confirm.isEmpty())
                throw new RuntimeException("Debe confirmar la nueva contraseña");

            if (!encoder.matches(current, usuario.getPassword())) {
                throw new RuntimeException("La contraseña es incorrecta");
            }
            if(!newPwd.equals(confirm)) {
                throw new RuntimeException("Las contraseñas nuevas no coinciden");
            }
            if(encoder.matches(newPwd, usuario.getPassword())) {
                throw new RuntimeException("Debe elegir una contraseña distinta");
            }
            String hashed = encoder.encode(newPwd);
            usuario.setPassword(hashed);
        }

        if (dto.getNombre() != null)
            usuario.setNombre(dto.getNombre());

        if (dto.getApellido() != null)
            usuario.setApellido(dto.getApellido());

        if (dto.getEmail() != null)
            usuario.setEmail(dto.getEmail());

        if (dto.getFechaNacimiento() != null)
            usuario.setFechaNacimiento(dto.getFechaNacimiento());

        return usuarioRepository.save(usuario);
    }


    public Usuario obtenerPorId(Long id) {
        return usuarios.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario", String.valueOf(id)));
    }

    private Usuario registroUsuarioDTO(RegistroUsuarioDTO dto){
        return Usuario.builder()
                .nombre(dto.getUsername())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .fechaNacimiento(dto.getFecha_nacimiento())
                .build();
    }

    public Boolean hayCambios(Usuario usuario, UsuarioInputDTO dto) {
        boolean cambioDatos = false;

        if(dto.getNombre() != null && !dto.getNombre().isEmpty() && !dto.getNombre().equals(usuario.getNombre()))
            cambioDatos = true;

        if(dto.getApellido() != null && !dto.getApellido().isEmpty() && !dto.getApellido().equals(usuario.getApellido()))
            cambioDatos = true;

        if(dto.getEmail() != null && !dto.getEmail().isEmpty() && !dto.getEmail().equals(usuario.getEmail()))
            cambioDatos = true;

        if(dto.getFechaNacimiento() != null && !dto.getFechaNacimiento().equals(usuario.getFechaNacimiento()))
            cambioDatos = true;

        boolean cambioPassword = (dto.getCurrentPassword() != null && !dto.getCurrentPassword().isEmpty()) ||
                        (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) ||
                        (dto.getConfirmNewPassword() != null && !dto.getConfirmNewPassword().isEmpty());

        return cambioDatos || cambioPassword;
    }
}
