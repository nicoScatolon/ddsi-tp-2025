package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultimediaUploadOutputDTO {
    private MultipartFile file;
    private TipoContenido tipoContenido;
    private String descripcion;
}
