package ar.edu.utn.frba.dds.fuenteEstatica.servicies.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.*;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.repository.impl.HechosRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.servicies.IHechosService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.ImportadorHechosCSV;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.repository.IHechosRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.servicies.impl.HechosService;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

public class HechosServiceTest {

    @Test
    void integraciónImportadorYServicio_leeCSVCompleto() throws URISyntaxException {
        // 1) Ruta al CSV en test/resources
        String csvPath = Paths.get(
                getClass().getClassLoader()
                        .getResource("desastres_naturales_argentina.csv")
                        .toURI()
        ).toString();

        // 2) Instancio repo, service e importador real
        IHechosRepository repo = new HechosRepository();
        IHechosService service = new HechosService(repo);
        ImportadorHechos importador = new ImportadorHechosCSV();

        // 3) Ejecuto la importación
        List<Hecho> hechos = service.importarArchivoHechos(csvPath, importador);

        // 4a) No devuelve lista vacía
        assertFalse(hechos.isEmpty(), "Debe importar al menos un hecho");

        // 4b) El repo contiene exactamente los mismos hechos
        assertEquals(hechos.size(), repo.findAll().size(),
                "Repositorio y lista de retorno deben coincidir en tamaño");

        // 4c) (Opcional) Verifico algún campo de la primera fila
        Hecho primero = hechos.get(0);
        assertNotNull(primero.getTitulo(), "El título del primer hecho no debe ser nulo");
        // p.ej. assertEquals("Inundación en la costa", primero.getTitulo());
    }
}
