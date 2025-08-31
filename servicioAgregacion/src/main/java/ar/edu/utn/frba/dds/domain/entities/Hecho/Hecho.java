package ar.edu.utn.frba.dds.domain.entities.Hecho;

import ar.edu.utn.frba.dds.domain.entities.*;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private List<Etiqueta> etiquetas = new ArrayList<>();
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;

    private IContenidoMultimedia contenidoMultimedia;

    private Long id;
    private Long origenId;

    private Boolean fueEliminado = false;

    private TipoHecho tipoHecho;
    private Contribuyente contribuyente = null;

    // metodos

    public void agregarEtiqueta(Etiqueta etiqueta){
        etiquetas.add(etiqueta);
    }

    public void eliminarEtiqueta(Etiqueta etiqueta){
        etiquetas.remove(etiqueta);
    }
}

