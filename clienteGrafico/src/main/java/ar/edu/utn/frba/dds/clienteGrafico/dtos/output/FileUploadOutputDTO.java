package ar.edu.utn.frba.dds.clienteGrafico.dtos.output;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.TipoContenido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadOutputDTO {
    private String filename;
    private String contentType;
    private String content;
    private String tipoContenido;
}
