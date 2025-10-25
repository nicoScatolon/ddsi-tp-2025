package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.ContenidoMultimediaOutputDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFileSystemService {
    void importarArchivoCSV(MultipartFile archivo) throws IOException;
    String construirUrlMultimedia(String rutaRelativa);
    List<ContenidoMultimediaOutputDTO> guardarContenidoMultimedia(List<MultipartFile> multimediaFiles,
                                                                  List<String> tiposContenido,
                                                                  List<String> descripciones) throws IOException;
}
