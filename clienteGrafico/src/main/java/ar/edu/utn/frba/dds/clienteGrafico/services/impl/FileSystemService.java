package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.ContenidoMultimediaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.FileUploadOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.ContenidoMultimediaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.TipoContenido;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFileSystemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class FileSystemService implements IFileSystemService {
    private final WebApiCallerService webApiCallerService;
    private final String fuenteEstaticaUrl;
    private final String fileSystemUrl;

    public FileSystemService(WebApiCallerService webApiCallerService,
                                 @Value("${fuente.estatica.url}") String fuenteEstaticaUrl,
                                 @Value("${file.system.url}") String fileSystemUrl) {
        this.webApiCallerService = webApiCallerService;
        this.fuenteEstaticaUrl = fuenteEstaticaUrl;
        this.fileSystemUrl = fileSystemUrl;
    }

    @Override
    public List<ContenidoMultimediaOutputDTO> guardarContenidoMultimedia(
            List<MultipartFile> multimediaFiles,
            List<String> tiposContenido,
            List<String> descripciones) throws IOException {

        List<ContenidoMultimediaOutputDTO> contenidoList = new ArrayList<>();

        for (int i = 0; i < multimediaFiles.size(); i++) {
            MultipartFile file = multimediaFiles.get(i);
            if (!file.isEmpty()) {
                try {
                    // Pasar el tipo de contenido al método de subida
                    String tipoContenidoStr = tiposContenido.get(i);
                    String url = subirArchivoAlFileSystem(file, tipoContenidoStr);

                    ContenidoMultimediaOutputDTO contenido = new ContenidoMultimediaOutputDTO();
                    contenido.setUrl(url);
                    contenido.setTipoContenido(TipoContenido.valueOf(tipoContenidoStr));

                    String descripcion = null;
                    if (descripciones != null && i < descripciones.size()) {
                        String desc = descripciones.get(i);
                        if (desc != null && !desc.trim().isEmpty()) {
                            descripcion = desc.trim();
                        }
                    }
                    contenido.setDescripcion(descripcion);

                    contenidoList.add(contenido);

                } catch (Exception e) {
                    System.err.println("Error al subir archivo " + file.getOriginalFilename() + ": " + e.getMessage());
                    throw new IOException("No se pudo subir el archivo: " + file.getOriginalFilename(), e);
                }
            }
        }

        return contenidoList;
    }

    @Override
    public void importarArchivoCSV(MultipartFile archivo) throws IOException {
        String urlGuardarArchivo = fileSystemUrl + "/api/file-system/csv";

        // Convertir bytes a Base64
        String base64Content = Base64.getEncoder().encodeToString(archivo.getBytes());

        // Convertir el MultipartFile a un DTO con Base64
        FileUploadOutputDTO fileDTO = FileUploadOutputDTO.builder()
                .contentType(archivo.getContentType())
                .content(base64Content)  // Ahora es String en Base64
                .filename(archivo.getOriginalFilename())
                .build();

        String rutaArchivo = webApiCallerService.post(urlGuardarArchivo, fileDTO, String.class);

        if (rutaArchivo == null || rutaArchivo.isEmpty()) {
            throw new IOException("No se pudo obtener la ruta del archivo desde el módulo filesystem.");
        }

        this.enviarRutaAFuenteEstatica(rutaArchivo);
    }

    @Override
    public void procesarImagenPrincipalListaHechos(List<HechoInputDTO> listaHechosDTO) {
        listaHechosDTO.forEach(hecho -> {
            if (hecho.getContenidoMultimedia() != null && !hecho.getContenidoMultimedia().isEmpty()) {
                // Buscar la primera imagen
                Optional<ContenidoMultimediaInputDTO> primeraImagen = hecho.getContenidoMultimedia().stream()
                        .filter(c -> c.getTipoContenido() == TipoContenido.IMAGEN)
                        .findFirst();

                if (primeraImagen.isPresent()) {
                    ContenidoMultimediaInputDTO imagen = primeraImagen.get();
                    String urlCompleta = this.construirUrlMultimedia(imagen.getUrl());
                    imagen.setUrl(urlCompleta);
                    hecho.setContenidoMultimedia(List.of(imagen));
                } else {
                    hecho.setContenidoMultimedia(Collections.emptyList());
                }
            }
        });
    }

    @Override
    public void procesarImagenPrincipalListaColecciones(List<ColeccionPreviewInputDTO> listaColeccionesDTO) {
        listaColeccionesDTO.forEach(coleccion -> {
            ContenidoMultimediaInputDTO contenido = coleccion.getContenidoMultimedia();

            if (contenido != null) {
                if (contenido.getTipoContenido() == TipoContenido.IMAGEN) {
                    String urlCompleta = this.construirUrlMultimedia(contenido.getUrl());
                    contenido.setUrl(urlCompleta);
                } else {
                    coleccion.setContenidoMultimedia(null);
                }
            }
        });
    }

    private String subirArchivoAlFileSystem(MultipartFile file, String tipoContenido) throws IOException {
        String urlFileSystem = fileSystemUrl + "/api/file-system/multimedia";

        String base64Content = Base64.getEncoder().encodeToString(file.getBytes());

        FileUploadOutputDTO fileDTO = FileUploadOutputDTO.builder()
                .contentType(file.getContentType())
                .content(base64Content)
                .filename(file.getOriginalFilename())
                .tipoContenido(tipoContenido) // NUEVO
                .build();

        try {
            String url = webApiCallerService.post(urlFileSystem, fileDTO, String.class);

            if (url == null || url.isEmpty()) {
                throw new IOException("Respuesta inválida del servicio de archivos");
            }

            return url;

        } catch (Exception e) {
            System.err.println("Error al subir archivo: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Error al comunicarse con el servicio de archivos: " + e.getMessage(), e);
        }
    }

    public String construirUrlMultimedia(String rutaRelativa) {
        return fileSystemUrl + "/api/file-system/multimedia/" + rutaRelativa;
    }

    private void enviarRutaAFuenteEstatica(String rutaArchivo) {
        String url = fuenteEstaticaUrl + "/api/fuenteEstatica/hechos?filename=" + rutaArchivo;

        try {
            webApiCallerService.post(url, new java.util.HashMap<>(), String.class);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al enviar el archivo a la fuente estática: " + e.getMessage(), e);
        }
    }
}
