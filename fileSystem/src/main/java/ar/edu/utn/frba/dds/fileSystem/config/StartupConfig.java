package ar.edu.utn.frba.dds.fileSystem.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class StartupConfig {

    @Value("${app.directorio.destino}")
    private String directorioDestino;

    @Value("${server.port}")
    private String serverPort;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        log.info("=================================================");
        log.info("FileSystem Application Starting...");
        log.info("=================================================");
        log.info("Server Port: {}", serverPort);
        log.info("Storage Directory: {}", directorioDestino);

        try {
            // Crear directorio base
            Path baseDir = Paths.get(directorioDestino);
            crearDirectorio(baseDir);

            // Crear subdirectorios necesarios
            crearDirectorio(baseDir.resolve("archivosCSV"));
            crearDirectorio(baseDir.resolve("multimedia"));
            crearDirectorio(baseDir.resolve("multimedia/fotos"));
            crearDirectorio(baseDir.resolve("multimedia/videos"));
            crearDirectorio(baseDir.resolve("multimedia/documentos"));
            crearDirectorio(baseDir.resolve("multimedia/audios"));
            crearDirectorio(baseDir.resolve("multimedia/otros"));

            log.info("✅ All directories created successfully");

        } catch (IOException e) {
            log.error("❌ Failed to create directories", e);
            throw new RuntimeException("Cannot create storage directories", e);
        }

        log.info("=================================================");
        log.info("✅ FileSystem Ready!");
        log.info("Health check: http://localhost:{}/actuator/health", serverPort);
        log.info("=================================================");
    }

    private void crearDirectorio(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            log.info("Created directory: {}", path.toAbsolutePath());
        } else {
            log.info("Directory already exists: {}", path.toAbsolutePath());
        }

        // Verificar permisos
        File dir = path.toFile();
        if (!dir.canRead() || !dir.canWrite()) {
            log.warn("⚠️ Directory {} may not have proper permissions", path);
        }
    }
}