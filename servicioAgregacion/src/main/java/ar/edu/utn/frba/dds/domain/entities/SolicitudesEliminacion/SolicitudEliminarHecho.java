package ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter

@Entity
@Table(name = "solicitud_eliminar_hecho")
public class SolicitudEliminarHecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "razonDeEliminacion")
    private String razonDeEliminacion;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hecho_id")
    private Hecho hecho;
    @Enumerated(EnumType.STRING)
    private EstadoDeSolicitud estado;
    @Column(name = "nombreCreador")
    private String nombreCreador;
    @Column(name = "apellidoCreador")
    private String apellidoCreador;
    //posible cambio por Contribuyente
    @Column(name = "nombreAdministrador")
    private String nombreAdministrador;
    @Column(name = "apellidoAdministrador")
    private String apellidoAdministrador;
    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;
    @Column(name = "fechaGestion")
    private LocalDateTime fechaGestion = null;
    @Column (name = "esEliminada", nullable = false)
    private boolean eliminada = false;
    @Column (name = "actualizarFuenteOrigen", nullable = false)
    private Boolean actualizarFuenteOrigen = false; //el actualizar con la fuente de origen

    public void serAceptada(String nombreAdministrador, String apellidoAdministrador) {
        if (estado != EstadoDeSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada.");
        }
        estado = EstadoDeSolicitud.ACEPTADA;
        fechaGestion = LocalDateTime.now();
        this.nombreAdministrador = nombreAdministrador;
        this.apellidoAdministrador = apellidoAdministrador;
        hecho.setFueEliminado(true);
        this.actualizarFuenteOrigen = true;
    }

    public void serRechazada(String nombreAdministrador, String apellidoAdministrador){
        if (estado != EstadoDeSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada.");
        }
        estado = EstadoDeSolicitud.RECHAZADA;
        this.nombreAdministrador = nombreAdministrador;
        this.apellidoAdministrador = apellidoAdministrador;
    }

}