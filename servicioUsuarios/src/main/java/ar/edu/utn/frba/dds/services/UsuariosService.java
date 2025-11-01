package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.models.dtos.RegistroUsuarioDTO;
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
}
