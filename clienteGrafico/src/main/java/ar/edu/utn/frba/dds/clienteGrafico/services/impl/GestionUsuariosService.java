package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.RolesPermisosDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.UsuarioDTO;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class GestionUsuariosService {
    private static final Logger log = LoggerFactory.getLogger(GestionUsuariosService.class);
    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String authServiceUrl;
    private final String usuariosServiceUrl;

    @Autowired
    public GestionUsuariosService(
            WebApiCallerService webApiCallerService,
            @Value("${auth.service.url}") String authServiceUrl,
            @Value("${usuarios.service.url}") String usuariosServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.authServiceUrl = authServiceUrl;
        this.usuariosServiceUrl = usuariosServiceUrl;
    }

    public AuthResponseDTO login(String username, String password) {
        try {
            AuthResponseDTO response = webClient
                    .post()
                    .uri(authServiceUrl + "/auth")
                    .bodyValue(Map.of(
                            "username", username,
                            "password", password
                    ))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
            return response;
        } catch (WebClientResponseException e) {
            log.error(e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // Login fallido - credenciales incorrectas
                return null;
            }
            // Otros errores HTTP
            throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
        }
    }

    public RolesPermisosDTO getRolesPermisos(String accessToken) {
        try {
            RolesPermisosDTO response = webApiCallerService.getWithAuth(
                    authServiceUrl + "/auth/user/roles-permisos",
                    accessToken,
                    RolesPermisosDTO.class
            );
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error al obtener roles y permisos: " + e.getMessage(), e);
        }
    }

    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        List<UsuarioDTO> response = webApiCallerService.getList(
                usuariosServiceUrl + "/usuarios",
                UsuarioDTO.class
        );
        return response != null ? response : List.of();
    }

    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        UsuarioDTO response = webApiCallerService.get(
                usuariosServiceUrl + "/usuarios/" + id,
                UsuarioDTO.class
        );
        if (response == null) {
            throw new NotFoundException("Usuario", String.valueOf(id));
        }
        return response;
    }

    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        UsuarioDTO response = webApiCallerService.post(
                usuariosServiceUrl + "/usuarios",
                usuarioDTO,
                UsuarioDTO.class
        );
        if (response == null) {
            throw new RuntimeException("Error al crear usuario en el servicio externo");
        }
        return response;
    }

    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        UsuarioDTO response = webApiCallerService.put(
                usuariosServiceUrl + "/usuarios/" + id,
                usuarioDTO,
                UsuarioDTO.class
        );
        if (response == null) {
            throw new RuntimeException("Error al actualizar usuario en el servicio externo");
        }
        return response;
    }

    public void eliminarUsuario(Long id) {
        webApiCallerService.delete(usuariosServiceUrl + "/usuarios/" + id);
    }

    public boolean existeUsuario(Long id) {
        try {
            obtenerUsuarioPorId(id);
            return true;
        } catch (NotFoundException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia del usuario: " + e.getMessage(), e);
        }
    }
}
