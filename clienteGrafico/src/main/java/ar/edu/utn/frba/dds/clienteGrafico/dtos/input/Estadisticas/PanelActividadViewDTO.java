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
    private List<ParClaveValorDTO> porProvincia;    // bar
    private List<ParClaveValorDTO> porCategoria;    // pie
    private SpamEliminacionDTO     spamEliminacion; // kpi

    public static PanelActividadViewDTO empty() {
        return PanelActividadViewDTO.builder()
                .porProvincia(Collections.emptyList())
                .porCategoria(Collections.emptyList())
                .spamEliminacion(new SpamEliminacionDTO(0, 0))
                .build();
    }
}
