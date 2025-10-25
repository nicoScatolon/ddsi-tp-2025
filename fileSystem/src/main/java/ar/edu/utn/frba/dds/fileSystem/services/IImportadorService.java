package ar.edu.utn.frba.dds.fileSystem.services;

import ar.edu.utn.frba.dds.fileSystem.dtos.Input.FileUploadInputDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImportadorService {
    String guardarArchivoCSV(FileUploadInputDTO fileDTO) throws IOException;
}
