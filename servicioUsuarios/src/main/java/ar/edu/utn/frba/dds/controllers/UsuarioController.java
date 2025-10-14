package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.dtos.RegistroAdminDTO;
import ar.edu.utn.frba.dds.models.dtos.RegistroContribuyenteDTO;
import ar.edu.utn.frba.dds.models.dtos.UsuarioMapper;
import ar.edu.utn.frba.dds.models.dtos.UsuarioResponseDTO;
import ar.edu.utn.frba.dds.models.entities.Usuario;
import ar.edu.utn.frba.dds.services.UsuariosService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuariosService usuarioService;


    // Público: registro de contribuyente
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody RegistroContribuyenteDTO dto) {
        Usuario u = usuarioService.registrarContribuyente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toResponse(u));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMINSUPERIOR')")
    public ResponseEntity<UsuarioResponseDTO> altaAdmin(@RequestBody RegistroAdminDTO dto) {
        Usuario u = usuarioService.altaAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toResponse(u));
    }


    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario u = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toResponse(u));
    }
}

