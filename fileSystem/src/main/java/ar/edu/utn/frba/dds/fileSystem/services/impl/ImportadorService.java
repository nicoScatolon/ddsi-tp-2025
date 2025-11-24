package ar.edu.utn.frba.dds.fileSystem.services.impl;

import ar.edu.utn.frba.dds.fileSystem.dtos.Input.FileUploadInputDTO;
import ar.edu.utn.frba.dds.fileSystem.services.IImportadorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;

@Slf4j
@Service
public class ImportadorService implements IImportadorService {

    @Value("${app.directorio.destino}")
    private String directorioBase;

    @Override
    public String guardarArchivoCSV(FileUploadInputDTO fileDTO) throws IOException {
        log.info("Guardando archivo CSV: {}", fileDTO.getFilename());

        if (fileDTO.getContent() == null || fileDTO.getContent().isEmpty()) {
            throw new IllegalArgumentException("Por favor selecciona un archivo CSV.");
        }

        String nombreArchivo = fileDTO.getFilename();
        if (nombreArchivo == null || !nombreArchivo.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("El archivo debe ser de tipo CSV.");
        }

        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(fileDTO.getContent());
        } catch (IllegalArgumentException e) {
            throw new IOException("El contenido del archivo no es Base64 válido: " + e.getMessage());
        }

        if (bytes.length == 0) {
            throw new IllegalArgumentException("El archivo decodificado está vacío.");
        }

        // Crear directorio si no existe
        Path directorioDestino = Paths.get(directorioBase, "archivosCSV");

        try {
            if (!Files.exists(directorioDestino)) {
                Files.createDirectories(directorioDestino);
                log.info("Directorio creado: {}", directorioDestino.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Error creando directorio CSV: {}", directorioDestino, e);
            throw new IOException("No se pudo crear el directorio: " + e.getMessage());
        }

        String nombreUnico = generarNombreUnico(nombreArchivo);
        Path rutaArchivo = directorioDestino.resolve(nombreUnico);

        try {
            Files.write(rutaArchivo, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("Archivo CSV guardado: {}", rutaArchivo.toAbsolutePath());
        } catch (IOException e) {
            log.error("Error escribiendo archivo CSV: {}", rutaArchivo, e);
            throw new IOException("No se pudo escribir el archivo: " + e.getMessage());
        }

        if (!Files.exists(rutaArchivo)) {
            throw new IOException("El archivo no se guardó correctamente.");
        }

        return rutaArchivo.toAbsolutePath().toString();
    }

    private String generarNombreUnico(String nombreOriginal) {
        // Sanitizar nombre: eliminar espacios y caracteres especiales
        nombreOriginal = nombreOriginal
                .replace(" ", "_")
                .replace("%", "")
                .replace("#", "")
                .replace("&", "")
                .replace("?", "")
                .replace("+", "");

        int lastDot = nombreOriginal.lastIndexOf('.');
        String nombreSinExtension = lastDot > 0 ? nombreOriginal.substring(0, lastDot) : nombreOriginal;
        String extension = lastDot > 0 ? nombreOriginal.substring(lastDot) : "";
        long timestamp = System.currentTimeMillis();

        return nombreSinExtension + "_" + timestamp + extension;
    }
}