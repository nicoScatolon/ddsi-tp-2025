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
    @Column(columnDefinition = "TEXT", name = "razonDeEliminacion")
    private String razonDeEliminacion;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hecho_id")
    private Hecho hecho;
    @Enumerated(EnumType.STRING)
    private EstadoDeSolicitud estado;
    @Column(name = "idCreador")
    private Long idCreador;
    //posible cambio por Contribuyente
    @Column(name = "idAdministrador")
    private Long idAdministrador;
    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;
    @Column(name = "fechaGestion")
    private LocalDateTime fechaGestion = null;
    @Column (name = "esEliminada", nullable = false)
    private Boolean eliminada = false;
    @Column (name = "actualizarFuenteOrigen", nullable = false)
    private Boolean actualizarFuenteOrigen = false; //el actualizar con la fuente de origen

    //TODO agregar algun parametro que me permita desde el servicio de estadisticas saber si es SPAM o no

    public void serAceptada(Long idAdministrador) {
        if (estado != EstadoDeSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada.");
        }
        estado = EstadoDeSolicitud.ACEPTADA;
        fechaGestion = LocalDateTime.now();
        this.idAdministrador = idAdministrador;
        hecho.setFueEliminado(true);
        this.actualizarFuenteOrigen = true;
    }

    public void serRechazada(Long idAdministrador){
        if (estado != EstadoDeSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada.");
        }
        estado = EstadoDeSolicitud.RECHAZADA;
        this.idAdministrador = idAdministrador;
    }
}