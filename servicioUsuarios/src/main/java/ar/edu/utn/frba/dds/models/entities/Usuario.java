package ar.edu.utn.frba.dds.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = true)
    private String username; //todo agregar al registro

    @Column(name = "contrasenia", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.CONTRIBUYENTE;

    @ElementCollection(targetClass = Permiso.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "usuario_permisos",
            joinColumns = @JoinColumn(name = "usuario_id")
    )

    @Column(name = "permiso", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Permiso> permisos;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
}

