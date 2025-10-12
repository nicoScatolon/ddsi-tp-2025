package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.models.dtos.RegistroAdminDTO;
import ar.edu.utn.frba.dds.models.dtos.RegistroContribuyenteDTO;
import ar.edu.utn.frba.dds.models.entities.Permiso;
import ar.edu.utn.frba.dds.models.entities.Rol;
import ar.edu.utn.frba.dds.models.entities.Usuario;
import ar.edu.utn.frba.dds.models.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuariosService {
    private final UsuarioRepository usuarios;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    @Transactional
    public Usuario registrarContribuyente(RegistroContribuyenteDTO dto) {
        if (usuarios.existsByNombre(dto.getUsername()))
            throw new IllegalArgumentException("Usuario ya existe");

        Usuario u = new Usuario();
        u.setUsername(dto.getUsername());
        u.setPassword(encoder.encode(dto.getPassword()));
        u.setNombre(dto.getNombre());
        u.setApellido(dto.getApellido());
        u.setEmail(dto.getEmail());
        u.setRol(Rol.CONTRIBUYENTE);
         //TODO: VER QUE PERMISOS USA EL CONTRIBUYENTE

        return usuarios.save(u);
    }

    @Transactional
    public Usuario altaAdmin(RegistroAdminDTO dto) {
        if (usuarios.existsByNombre(dto.getUsername()))
            throw new IllegalArgumentException("Usuario ya existe");

        Usuario u = new Usuario();
        u.setNombre(dto.getUsername());
        u.setPassword(encoder.encode(dto.getPassword()));
        u.setRol(Rol.ADMIN);
        u.setNombre(dto.getUsername());
        u.setApellido(dto.getApellido());
        u.setEmail(dto.getEmail());
        u.setPermisos(Arrays.stream(Permiso.values())
                .collect(Collectors.toCollection(ArrayList::new)));
        return usuarios.save(u);
    }
}
