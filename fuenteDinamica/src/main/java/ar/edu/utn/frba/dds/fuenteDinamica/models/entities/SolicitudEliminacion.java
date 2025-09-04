package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter

@Entity
@Table(name = "solicitud_eliminacion")
public class SolicitudEliminacion {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hecho_id")
    private Hecho hecho;
    @Column(name = "razonDeEliminacion", nullable = false)
    private String razonDeEliminacion;
    @Embedded
    private Contribuyente creador;
    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "idAdmin")
    private Long idAdmin;
    @Column(name = "fechaGestion")
    private LocalDateTime fechaGestion = null;
    @Enumerated(EnumType.STRING)
    private EstadoSolicitudEliminacion estado;
    //TODO consultar que hacer con las solicitudes de eliminacion, ya que las guardamos en el agregador, si aca son necesarias o que
}
