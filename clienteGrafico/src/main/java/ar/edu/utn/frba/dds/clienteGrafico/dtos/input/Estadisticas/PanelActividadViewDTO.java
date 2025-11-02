package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas;

import lombok.*;
import java.util.Collections;
import java.util.List;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas.MayorProvPorColeccionInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas.MayorProvPorCatInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas.SolicitudesSpamInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas.ParClaveValorDTO;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PanelActividadViewDTO {

    // ---- RAW, idéntico a lo que trae el micro ----
    private List<MayorProvPorCatInputDTO> mayorProvPorCategoria;      // suele ser una lista (una por cada categoría)
    private MayorProvPorColeccionInputDTO mayorProvPorColeccion;      // un único objeto
    private SolicitudesSpamInputDTO       solicitudesSpam;            // un único objeto

    // ---- Derivados "listos para pintar" ----
    // Por categoría: x eje = categorías, y eje = cantHechosProvincia de la "mayor provincia" para cada categoría
    private List<ParClaveValorDTO> topProvinciaPorCategoria;

    // KPI de spam: (spam, total)
    private Integer spam;
    private Integer totalSolicitudes;

    public static PanelActividadViewDTO empty() {
        return PanelActividadViewDTO.builder()
                .mayorProvPorCategoria(Collections.emptyList())
                .mayorProvPorColeccion(null)
                .solicitudesSpam(new SolicitudesSpamInputDTO(null, 0, 0, null))
                .topProvinciaPorCategoria(Collections.emptyList())
                .spam(0)
                .totalSolicitudes(0)
                .build();
    }
}
