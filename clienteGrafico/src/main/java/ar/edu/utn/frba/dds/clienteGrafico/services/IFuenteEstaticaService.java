package ar.edu.utn.frba.dds.clienteGrafico.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface IFuenteEstaticaService {
    void importarArchivoCSV(MultipartFile archivo) throws IOException;
    String guardarArchivoCSV(Path rutaArchivo);
}
