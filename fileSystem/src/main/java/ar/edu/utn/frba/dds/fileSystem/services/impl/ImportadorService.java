package ar.edu.utn.frba.dds.fileSystem.services.impl;

import ar.edu.utn.frba.dds.fileSystem.dtos.Input.FileUploadInputDTO;
import ar.edu.utn.frba.dds.fileSystem.services.IImportadorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;

@Service
public class ImportadorService implements IImportadorService {
    @Value("${app.directorio.destino}")
    private String directorioBase;

    @Override
    public String guardarArchivoCSV(FileUploadInputDTO fileDTO) throws IOException {
        // Validar que el contenido Base64 no esté vacío
        if (fileDTO.getContent() == null || fileDTO.getContent().isEmpty()) {
            throw new IllegalArgumentException("Por favor selecciona un archivo CSV.");
        }

        String nombreArchivo = fileDTO.getFilename();
        if (nombreArchivo == null || !nombreArchivo.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("El archivo debe ser de tipo CSV.");
        }

        // Decodificar Base64 a bytes
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
        if (!Files.exists(directorioDestino)) {
            Files.createDirectories(directorioDestino);
        }

        // Generar nombre único y ruta
        String nombreUnico = generarNombreUnico(nombreArchivo);
        Path rutaArchivo = directorioDestino.resolve(nombreUnico);

        // Escribir el archivo desde los bytes decodificados
        Files.write(rutaArchivo, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // Verificar que se guardó
        if (!Files.exists(rutaArchivo)) {
            throw new IOException("El archivo no se guardó correctamente.");
        }

        return rutaArchivo.toAbsolutePath().toString();
    }

    private String generarNombreUnico(String nombreOriginal) {
        String nombreSinExtension = nombreOriginal.substring(0, nombreOriginal.lastIndexOf('.'));
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf('.'));
        long timestamp = System.currentTimeMillis();
        return nombreSinExtension + "_" + timestamp + extension;
    }
}
