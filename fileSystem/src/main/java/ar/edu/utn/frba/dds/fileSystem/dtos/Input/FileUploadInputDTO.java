package ar.edu.utn.frba.dds.fileSystem.dtos.Input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadInputDTO {
    private String filename;
    private String contentType;
    private String content;
    private String tipoContenido;
}
