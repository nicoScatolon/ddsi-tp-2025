package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionPreviewInputDTO {
    private String handle;
    private String titulo;
    private String descripcion;
    // tipo de curacion
    // lista fuentes
}
