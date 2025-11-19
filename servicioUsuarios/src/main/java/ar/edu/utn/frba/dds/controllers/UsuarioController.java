package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.dtos.RegistroUsuarioDTO;
import ar.edu.utn.frba.dds.models.dtos.UsuarioInputDTO;
import ar.edu.utn.frba.dds.models.dtos.UsuarioMapper;
import ar.edu.utn.frba.dds.models.dtos.UsuarioResponseDTO;
import ar.edu.utn.frba.dds.models.entities.Rol;
import ar.edu.utn.frba.dds.models.entities.Usuario;
import ar.edu.utn.frba.dds.services.UsuariosService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@AllArgsConstructor
public class UsuarioController {
    private final UsuariosService usuarioService;

    // Público: registro de contribuyente
    @PostMapping("/publica/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody RegistroUsuarioDTO dto) {
        Usuario u = usuarioService.registrarUsuario(dto, Rol.CONTRIBUYENTE);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toResponse(u));
    }

    @PostMapping("/privada/registrar/admin")
    @PreAuthorize("hasRole('ADMINSUPERIOR')")
    public ResponseEntity<UsuarioResponseDTO> altaAdmin(@RequestBody RegistroUsuarioDTO dto) {
        Usuario u = usuarioService.registrarUsuario(dto, Rol.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toResponse(u));
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<?> actualizarUsuario(@PathVariable("id") Long id, @RequestBody UsuarioInputDTO usuarioDTO) {
        try{
            Usuario u = usuarioService.actualizarUsuario(id,usuarioDTO);
            return ResponseEntity.ok(UsuarioMapper.toResponse(u));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/publica/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario u = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toResponse(u));
    }
}

