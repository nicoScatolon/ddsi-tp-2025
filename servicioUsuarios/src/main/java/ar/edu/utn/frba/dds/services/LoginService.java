package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.models.entities.Usuario;
import ar.edu.utn.frba.dds.models.repositories.UsuarioRepository;
import ar.edu.utn.frba.dds.models.dtos.UserRolesPermissionsDTO;
import ar.edu.utn.frba.dds.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.utils.JwtUtil;
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

    public Usuario autenticar(String username, String contrasenia) {
        Optional<Usuario> usuarioOpcional = usuariosRepository.findByEmail(username);

        if (usuarioOpcional.isEmpty()) {
            throw new NotFoundException("Usuario", username);
        }

        Usuario usuario = usuarioOpcional.get();

        // Verificar la contraseña usando BCrypt
        if (!passwordEncoder.matches(contrasenia, usuario.getPassword())) {
            throw new NotFoundException("Usuario", username);
        }

        return usuario;
    }

    // USAR EN LOGIN: ya tenés el Usuario cargado
    public String generarAccessToken(Usuario usuario) {
        return JwtUtil.generarAccessToken(usuario);
    }

    // USAR EN REFRESH: solo tenés el username; cargás y delegás
    public String generarAccessToken(String username) {
        var u = usuariosRepository.findByEmailFetchAuth(username)
                .orElseThrow(() -> new NotFoundException("Usuario", username));
        return JwtUtil.generarAccessToken(u);
    }

    // Nos conviene hacer esto porque en el login ya tenemos al usuario en memoria entonces es al pedo ir al repo y consultarle a la base de vuelta.
    // Si estamos generando el access a partir del refresh si vamos a necesitar ir a buscar el usuario al repo para generar el access token


    public String generarRefreshToken(String username) {
        return JwtUtil.generarRefreshToken(username);
    }

    public UserRolesPermissionsDTO obtenerRolesYPermisosUsuario(String username) {
        Usuario u = getByEmail(username);
        return UserRolesPermissionsDTO.builder()
                .userId(u.getId())
                .username(u.getNombre())
                .rol(u.getRol())
                .permisos(u.getPermisos())
                .build();
    }

    public Usuario getByEmail(String username) {
        return usuariosRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("Usuario", username));
    }
}
