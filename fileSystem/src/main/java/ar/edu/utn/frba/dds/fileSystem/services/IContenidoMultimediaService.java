package ar.edu.utn.frba.dds.fileSystem.services;

import ar.edu.utn.frba.dds.fileSystem.dtos.Input.FileUploadInputDTO;

import java.io.IOException;

public interface IContenidoMultimediaService {
    String guardarArchivoMultimedia(FileUploadInputDTO fileDTO) throws IOException;
    String obtenerArchivoMultimedia(String rutaRelativa) throws IOException;
}
