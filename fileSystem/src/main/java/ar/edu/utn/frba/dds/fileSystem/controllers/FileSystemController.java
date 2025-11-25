package ar.edu.utn.frba.dds.fileSystem.controllers;

import ar.edu.utn.frba.dds.fileSystem.dtos.Input.FileUploadInputDTO;
import ar.edu.utn.frba.dds.fileSystem.services.IContenidoMultimediaService;
import ar.edu.utn.frba.dds.fileSystem.services.IImportadorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/file-system")
public class FileSystemController {
    private final IContenidoMultimediaService contenidoMultimediaService;
    private final IImportadorService importadorService;

    public FileSystemController(IContenidoMultimediaService contenidoMultimediaService, IImportadorService importadorService){
        this.contenidoMultimediaService = contenidoMultimediaService;
        this.importadorService = importadorService;
    }

    @PostMapping("/csv")
    public ResponseEntity<String> guardarArchivoCSV(@RequestBody FileUploadInputDTO fileDTO) throws IOException {
        String ruta = importadorService.guardarArchivoCSV(fileDTO);
        return ResponseEntity.ok(ruta);
    }

    @PostMapping("/multimedia")
    public ResponseEntity<String> guardarArchivoMultimedia(@RequestBody FileUploadInputDTO fileDTO) throws IOException {
        String ruta = contenidoMultimediaService.guardarArchivoMultimedia(fileDTO);
        return ResponseEntity.ok(ruta);
    }

    @GetMapping("/multimedia/**")
    public ResponseEntity<byte[]> obtenerArchivoMultimedia(HttpServletRequest request) throws IOException {
        String fullPath = request.getRequestURI();

        int index = fullPath.indexOf("/multimedia/");
        if (index == -1) {
            return ResponseEntity.badRequest().build();
        }

        String rutaSolicitada = fullPath.substring(index + "/multimedia/".length());

        // ⭐ DECODIFICAR LA URL AQUÍ
        try {
            rutaSolicitada = java.net.URLDecoder.decode(rutaSolicitada, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Error al decodificar URL: " + rutaSolicitada);
        }

        System.out.println("=== Solicitando archivo (decodificado): " + rutaSolicitada);

        String rutaCompleta = contenidoMultimediaService.obtenerArchivoMultimedia(rutaSolicitada);

        Path path = Paths.get(rutaCompleta);

        if (!Files.exists(path)) {
            System.out.println("=== Archivo no encontrado: " + path.toAbsolutePath());
            return ResponseEntity.notFound().build();
        }

        byte[] archivo = Files.readAllBytes(path);

        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        System.out.println("=== Archivo encontrado, tamaño: " + archivo.length + " bytes, tipo: " + contentType);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(archivo);
    }
}
