package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.ContenidoMultimediaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.ContenidoMultimediaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.TipoContenido;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface IFileSystemService {
    void importarArchivoCSV(MultipartFile archivo) throws IOException;
    String construirUrlMultimedia(String rutaRelativa);
    List<ContenidoMultimediaOutputDTO> guardarContenidoMultimedia(List<MultipartFile> multimediaFiles,
                                                                  List<String> tiposContenido,
                                                                  List<String> descripciones) throws IOException;

    void procesarImagenPrincipalListaHechos(List<HechoInputDTO> listaHechosDTO);

    void procesarImagenPrincipalListaColecciones(List<ColeccionPreviewInputDTO> listaColeccionesDTO);


}
