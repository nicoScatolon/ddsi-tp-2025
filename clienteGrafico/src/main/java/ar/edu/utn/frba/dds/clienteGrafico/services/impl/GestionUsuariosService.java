package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.UsuarioInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Usuarios.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Usuarios.RegisterUsuarioRequestDTO;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.IGestionUsuariosService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GestionUsuariosService implements IGestionUsuariosService {
    private static final Logger log = LoggerFactory.getLogger(GestionUsuariosService.class);
    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String authServiceUrl;

    @Autowired
    public GestionUsuariosService(
            WebApiCallerService webApiCallerService,
            @Value("${auth.service.url}") String authServiceUrl){
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.authServiceUrl = authServiceUrl;
    }

    @Override
    public AuthResponseDTO login(String username, String password) {
        try {
            return webApiCallerService.postPublic(
                    authServiceUrl + "/auth",
                    Map.of("username", username, "password", password),
                    AuthResponseDTO.class
            );
        } catch (NotFoundException e) {
            // Credenciales inválidas (mantenemos tu contrato de devolver null)
            return null;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
        }
    }

    @Override
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

    @Override
    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() { //Todo revisar servUsuarios
        List<UsuarioResponseDTO> response = webApiCallerService.getList(
                authServiceUrl + "/usuarios",
                UsuarioResponseDTO.class
        );
        return response != null ? response : List.of();
    }

    @Override
    public UsuarioInputDTO obtenerUsuarioPorId(Long id) {
        UsuarioInputDTO response = webApiCallerService.get(
                authServiceUrl + "/usuarios/publica/" + id,
                UsuarioInputDTO.class
        );
        if (response == null) {
            throw new NotFoundException("Usuario", String.valueOf(id));
        }
        return response;
    }

    @Override
    public UsuarioResponseDTO crearUsuario(RegisterUsuarioRequestDTO usuarioDTO) {
        UsuarioResponseDTO response = webApiCallerService.postPublic(
                authServiceUrl + "/usuarios/publica/registrar",
                usuarioDTO,
                UsuarioResponseDTO.class
        );
        if (response == null) {
            throw new RuntimeException("Error al crear usuario en el servicio externo");
        }
        return response;
    }


    @Override
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioOutputDTO usuarioDTO) {
        UsuarioResponseDTO response = webApiCallerService.put(
                authServiceUrl + "/usuarios/" + id,
                usuarioDTO,
                UsuarioResponseDTO.class
        );
        if (response == null) {
            throw new RuntimeException("Error al actualizar usuario en el servicio externo");
        }
        return response;
    }

    @Override
    public void eliminarUsuario(Long id) { //Todo revisar servUsuarios
        webApiCallerService.delete(authServiceUrl + "/usuarios/" + id);
    }

    @Override
    public Boolean existeUsuario(Long id) {
        try {
            obtenerUsuarioPorId(id);
            return true;
        } catch (NotFoundException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia del usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public UsuarioResponseDTO crearAdmin(RegisterUsuarioRequestDTO usuario) {
        UsuarioResponseDTO response = webApiCallerService.post(
                authServiceUrl + "/usuarios/privada/registrar/admin",
                usuario,
                UsuarioResponseDTO.class
        );
        if (response == null) {
            throw new RuntimeException("Error al crear usuario en el servicio externo");
        }
        return response;
    }
}
