package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteEstaticaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FuenteEstaticaService implements IFuenteEstaticaService {
    private final WebApiCallerService webApiCallerService;
    private final String fuenteEstaticaUrl;

    public FuenteEstaticaService(WebApiCallerService webApiCallerService,
                                 @Value("${fuente.estatica.url}") String fuenteEstaticaUrl) {
        this.webApiCallerService = webApiCallerService;
        this.fuenteEstaticaUrl = fuenteEstaticaUrl;
    }

    @Value("${app.directorio.destino}")
    private String directorioBase;

    @Override
    public void importarArchivoCSV(MultipartFile archivo) throws IOException {
        // === Validaciones básicas ===
        if (archivo.isEmpty()) {
            throw new IllegalArgumentException("Por favor selecciona un archivo CSV.");
        }

        String nombreArchivo = archivo.getOriginalFilename();
        if (nombreArchivo == null || !nombreArchivo.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("El archivo debe ser de tipo CSV.");
        }

        // === Construcción de rutas ===
        Path directorioDestino = Paths.get(directorioBase, "archivosCSV");
        if (!Files.exists(directorioDestino)) {
            Files.createDirectories(directorioDestino);
        }

        String nombreUnico = generarNombreUnico(nombreArchivo);
        Path rutaArchivo = directorioDestino.resolve(nombreUnico);

        // === Copiar archivo ===
        try (InputStream inputStream = archivo.getInputStream()) {
            Files.copy(inputStream, rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        }

        // Verificar que se guardó
        if (!Files.exists(rutaArchivo)) {
            throw new IOException("El archivo no se guardó correctamente.");
        }

        // === Llamada al servicio de fuente estática ===
        this.guardarArchivoCSV(rutaArchivo);
    }

    @Override
    public String guardarArchivoCSV(Path rutaArchivo) {
        // Convertir Path a String absoluto
        String rutaAbsoluta = rutaArchivo.toAbsolutePath().toString();

        String url = fuenteEstaticaUrl + "/api/fuenteEstatica/hechos?filename=" + rutaAbsoluta;

        try {
            return webApiCallerService.post(url, 0 , String.class);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al obtener el hecho mapa: " + e.getMessage(), e);
        }
    }

    private String generarNombreUnico(String nombreOriginal) {
        String nombreSinExtension = nombreOriginal.substring(0, nombreOriginal.lastIndexOf('.'));
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf('.'));
        long timestamp = System.currentTimeMillis();
        return nombreSinExtension + "_" + timestamp + extension;
    }
}