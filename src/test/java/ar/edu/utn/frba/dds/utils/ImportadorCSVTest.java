package ar.edu.utn.frba.dds.utils;



import ar.edu.utn.frba.dds.Hechos.Hecho;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;


class ImportadorCSVTest {

    @Test
    public void verificarImportacionCrearArchivos(){
        String path = "C:\\Users\\Lucio.DESKTOP-6SBRF0T\\Desktop\\UTN\\tercer_anio\\DSI\\2025-tpa-ma-ma-grupo-11\\desastres_naturales_argentina.csv";
        ImportadorHechosCSV importadorCSV = new ImportadorHechosCSV();
        Set<Hecho> listaHechos;
        listaHechos = importadorCSV.importarHechosArchivo(path);

        Assertions.assertTrue(listaHechos.size()>1);
        //verificamos los datos de cada hecho con el debugger
    }
}


