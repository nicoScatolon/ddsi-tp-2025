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

    public Usuario autenticar(String nombre, String contrasenia) {
        Optional<Usuario> usuarioOpcional = usuariosRepository.findByNombre(nombre);

        if (usuarioOpcional.isEmpty()) {
            throw new NotFoundException("Usuario", nombre);
        }

        Usuario usuario = usuarioOpcional.get();

        // Verificar la contraseña usando BCrypt
        if (!passwordEncoder.matches(contrasenia, usuario.getPassword())) {
            throw new NotFoundException("Usuario", nombre);
        }

        return usuario;
    }

    // USAR EN LOGIN: ya tenés el Usuario cargado
    public String generarAccessToken(Usuario usuario) {
        return JwtUtil.generarAccessToken(usuario);
    }

    // USAR EN REFRESH: solo tenés el username; cargás y delegás
    public String generarAccessToken(String username) {
        var u = usuariosRepository.findByNombreFetchAuth(username)
                .orElseThrow(() -> new NotFoundException("Usuario", username));
        return JwtUtil.generarAccessToken(u);
    }

    // Nos conviene hacer esto porque en el login ya tenemos al usuario en memoria entonces es al pedo ir al repo y consultarle a la base de vuelta.
    // Si estamos generando el access a partir del refresh si vamos a necesitar ir a buscar el usuario al repo para generar el access token


    public String generarRefreshToken(String username) {
        return JwtUtil.generarRefreshToken(username);
    }

    public UserRolesPermissionsDTO obtenerRolesYPermisosUsuario(String username) {
        var u = getByUsername(username);
        return UserRolesPermissionsDTO.builder()
                .username(u.getNombre())
                .rol(u.getRol())
                .permisos(u.getPermisos())
                .build();
    }

    public Usuario getByUsername(String username) {
        return usuariosRepository.findByNombre(username)
                .orElseThrow(() -> new NotFoundException("Usuario", username));
    }
}
