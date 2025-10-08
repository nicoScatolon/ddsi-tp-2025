package ar.edu.utn.frrba.dds.servicioUsuarios.services;

import ar.edu.utn.frrba.dds.servicioUsuarios.models.repositories.UsuarioRepository;
import ar.edu.utn.frrba.dds.servicioUsuarios.models.dtos.UserRolesPermissionsDTO;
import ar.edu.utn.frrba.dds.servicioUsuarios.exceptions.NotFoundException;
import ar.edu.utn.frrba.dds.servicioUsuarios.models.entities.Usuario;
import ar.edu.utn.frrba.dds.servicioUsuarios.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final UsuarioRepository usuariosRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginService(UsuarioRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario autenticarUsuario(String username, String password) {
        Optional<Usuario> usuarioOpt = usuariosRepository.findByNombre(username);

        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", username);
        }

        Usuario usuario = usuarioOpt.get();

        // Verificar la contraseña usando BCrypt
        if (!passwordEncoder.matches(password, usuario.getContrasenia())) {
            throw new NotFoundException("Usuario", username);
        }

        return usuario;
    }

    public String generarAccessToken(String username) {
        return JwtUtil.generarAccessToken(username);
    }

    public String generarRefreshToken(String username) {
        return JwtUtil.generarRefreshToken(username);
    }

    public UserRolesPermissionsDTO obtenerRolesYPermisosUsuario(String username) {
        Usuario usuario = usuariosRepository.findByNombre(username)
                .orElseThrow(() -> new NotFoundException("Usuario", username));

        return UserRolesPermissionsDTO.builder()
                .username(usuario.getNombre())          // <- NO usuario.get()
                .rol(usuario.getRol())                  // Rol (enum) único
                .permisos(usuario.getPermisos())        // Set<Permiso>
                .build();
    }
}
