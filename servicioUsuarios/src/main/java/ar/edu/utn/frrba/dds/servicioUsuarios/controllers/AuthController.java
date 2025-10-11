package ar.edu.utn.frrba.dds.servicioUsuarios.controllers;

import ar.edu.utn.frrba.dds.servicioUsuarios.exceptions.NotFoundException;
import ar.edu.utn.frrba.dds.servicioUsuarios.models.dtos.*;
import ar.edu.utn.frrba.dds.servicioUsuarios.models.entities.Usuario;
import ar.edu.utn.frrba.dds.servicioUsuarios.services.LoginService;
import ar.edu.utn.frrba.dds.servicioUsuarios.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<AuthResponseDTO> loginApi(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            log.info("Llegan este usuario y esta contrasenia" + username, password);

            // Validación básica de credenciales
            if (username == null || username.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            log.info("el usuario no se autentico");
            // Autenticar usuario usando el LoginService
            Usuario u = loginService.autenticar(username, password);
            log.info("el usuario autenticado");
            // Generar tokens
            String accessToken  = loginService.generarAccessToken(u);
            String refreshToken = loginService.generarRefreshToken(u.getNombre());

            log.info("se generaron los tokens, queda buildear la respuesta");
            AuthResponseDTO response = AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            log.info("El usuario {} está logueado. El token generado es {}", username, accessToken);

            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody RefreshRequestDTO request) {
        try {
            String username = JwtUtil.validarToken(request.getRefreshToken());

            // Validar que el token sea de tipo refresh
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.getKey())
                    .build()
                    .parseClaimsJws(request.getRefreshToken())
                    .getBody();

            if (!"refresh".equals(claims.get("type"))) {
                return ResponseEntity.badRequest().build();
            }


            // Generá el access con rol/perms resolviendo el usuario adentro del servicio
            String newAccessToken = loginService.generarAccessToken(username);

            TokenResponseDTO response = new TokenResponseDTO(newAccessToken, request.getRefreshToken());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/roles-permisos")
    public ResponseEntity<UserRolesPermissionsDTO> getUserRolesAndPermissions(Authentication authentication) {
        try {
            String username = authentication.getName();
            UserRolesPermissionsDTO response = loginService.obtenerRolesYPermisosUsuario(username);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Usuario no encontrado", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener roles y permisos del usuario", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
