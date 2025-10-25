package ar.edu.utn.frba.dds.fileSystem.services.impl;

import ar.edu.utn.frba.dds.fileSystem.dtos.Input.FileUploadInputDTO;
import ar.edu.utn.frba.dds.fileSystem.services.IContenidoMultimediaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

@Service
public class ContenidoMultimediaService implements IContenidoMultimediaService {
    @Value("${app.directorio.destino}")
    private String directorioBase;

    @Override
    public String guardarArchivoMultimedia(FileUploadInputDTO fileDTO) throws IOException {
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
        if (!Files.exists(directorioDestino)) {
            Files.createDirectories(directorioDestino);
        }

        String nombreUnico = generarNombreUnico(nombreArchivo);
        Path rutaArchivo = directorioDestino.resolve(nombreUnico);

        Files.write(rutaArchivo, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        if (!Files.exists(rutaArchivo)) {
            throw new IOException("El archivo no se guardó correctamente.");
        }

        return subcarpeta + "/" + nombreUnico;
    }

    @Override
    public String obtenerArchivoMultimedia(String rutaRelativa) throws IOException {
        System.out.println("======================");
        System.out.println("🔍 obtenerArchivoMultimedia");
        System.out.println("Directorio base: " + directorioBase);
        System.out.println("Ruta relativa: " + rutaRelativa);

        // Normalizar las barras para Windows
        rutaRelativa = rutaRelativa.replace("/", File.separator);

        Path rutaArchivo = Paths.get(directorioBase, "multimedia", rutaRelativa);
        System.out.println("Path construido: " + rutaArchivo.toAbsolutePath());
        System.out.println("Existe: " + Files.exists(rutaArchivo));
        System.out.println("======================");

        if (!Files.exists(rutaArchivo)) {
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
        String nombreSinExtension = nombreOriginal.substring(0, nombreOriginal.lastIndexOf('.'));
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf('.'));
        long timestamp = System.currentTimeMillis();
        return nombreSinExtension + "_" + timestamp + extension;
    }
}
