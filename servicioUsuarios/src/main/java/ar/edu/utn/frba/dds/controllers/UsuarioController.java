package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.dtos.RegistroAdminDTO;
import ar.edu.utn.frba.dds.models.dtos.RegistroContribuyenteDTO;
import ar.edu.utn.frba.dds.models.entities.Usuario;
import ar.edu.utn.frba.dds.services.UsuariosService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuariosService usuarioService;


    // Público: registro de contribuyente
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar( @RequestBody RegistroContribuyenteDTO dto) {
        try {
            Usuario u = usuarioService.registrarContribuyente(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("id", u.getId(), "username", u.getNombre()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage())); // 409 duplicado
        }
    }

    // Back-office: solo ADMIN
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMINSUPERIOR')")
    public ResponseEntity<?> altaAdmin(@RequestBody RegistroAdminDTO dto) {
        Usuario u = usuarioService.altaAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("id", u.getId(), "username", u.getNombre()));
    }
}
