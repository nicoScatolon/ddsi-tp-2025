package ar.edu.utn.frba.dds.fileSystem.services.impl;

import ar.edu.utn.frba.dds.fileSystem.dtos.Input.FileUploadInputDTO;
import ar.edu.utn.frba.dds.fileSystem.services.IContenidoMultimediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

@Slf4j
@Service
public class ContenidoMultimediaService implements IContenidoMultimediaService {

    @Value("${app.directorio.destino}")
    private String directorioBase;

    @Override
    public String guardarArchivoMultimedia(FileUploadInputDTO fileDTO) throws IOException {
        log.info("Guardando archivo multimedia: {}", fileDTO.getFilename());

        if (fileDTO.getContent() == null || fileDTO.getContent().isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío.");
        }

        String nombreArchivo = fileDTO.getFilename();
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo es inválido.");
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

        // Determinar subcarpeta según tipo de contenido
        String subcarpeta = determinarSubcarpeta(fileDTO.getTipoContenido());

        // Crear directorio para multimedia/subcarpeta
        Path directorioDestino = Paths.get(directorioBase, "multimedia", subcarpeta);

        try {
            if (!Files.exists(directorioDestino)) {
                Files.createDirectories(directorioDestino);
                log.info("Directorio creado: {}", directorioDestino.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Error creando directorio: {}", directorioDestino, e);
            throw new IOException("No se pudo crear el directorio: " + e.getMessage());
        }

        String nombreUnico = generarNombreUnico(nombreArchivo);
        Path rutaArchivo = directorioDestino.resolve(nombreUnico);

        try {
            Files.write(rutaArchivo, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("Archivo guardado: {}", rutaArchivo.toAbsolutePath());
        } catch (IOException e) {
            log.error("Error escribiendo archivo: {}", rutaArchivo, e);
            throw new IOException("No se pudo escribir el archivo: " + e.getMessage());
        }

        if (!Files.exists(rutaArchivo)) {
            throw new IOException("El archivo no se guardó correctamente.");
        }

        return subcarpeta + "/" + nombreUnico;
    }

    @Override
    public String obtenerArchivoMultimedia(String rutaRelativa) throws IOException {
        log.info("Obteniendo archivo multimedia: {}", rutaRelativa);
        log.info("Directorio base: {}", directorioBase);

        // Normalizar las barras para el sistema operativo
        rutaRelativa = rutaRelativa.replace("/", File.separator);

        Path rutaArchivo = Paths.get(directorioBase, "multimedia", rutaRelativa);
        log.info("Path completo: {}", rutaArchivo.toAbsolutePath());
        log.info("Existe: {}", Files.exists(rutaArchivo));

        if (!Files.exists(rutaArchivo)) {
            log.error("Archivo no encontrado: {}", rutaArchivo.toAbsolutePath());
            throw new IOException("El archivo no existe: " + rutaRelativa);
        }

        return rutaArchivo.toAbsolutePath().toString();
    }

    private String determinarSubcarpeta(String tipoContenido) {
        if (tipoContenido == null) {
            return "otros";
        }

        switch (tipoContenido.toUpperCase()) {
            case "FOTO":
            case "IMAGEN":
                return "fotos";
            case "VIDEO":
                return "videos";
            case "DOCUMENTO":
                return "documentos";
            case "AUDIO":
                return "audios";
            default:
                return "otros";
        }
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