package ar.edu.utn.frba.dds.utils;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.Hechos.Hecho;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ImportadorCSVTest {

    @Test
    public void verificarImportacionCreaArchivos(){
        String path = "C:\\Proyectos\\UTN\\DDS\\2025-tpa-ma-ma-grupo-11\\basesDeDatos\\desastres_naturales_argentina.csv";
        ImportadorCSV importadorCSV = new ImportadorCSV();
        Set<Hecho> listaHechos;
        listaHechos = importadorCSV.importarArchivo(path);

        Assertions.assertTrue(listaHechos.size()>1);
    }
}


