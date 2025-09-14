package ar.edu.utn.frba.dds.fuenteEstatica.domain.entities;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;

@Component
public class ImportadorHechosCSV implements ImportadorHechos {

    @Override

    public FormatoFuente getFormato() { return FormatoFuente.CSV; }

    @Override
    public List<Hecho> importarHechosArchivo(String path) {
        List<Hecho> listaHechos = new ArrayList<>();
        LocalDateTime fechaDeCarga = LocalDateTime.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            String[] fila;
            reader.readNext(); // Salteo el encabezado

            while ((fila = reader.readNext()) != null) {
                Hecho hecho = crearHechoFila(fila, fechaDeCarga, formatoFecha);
                verificarRepetido(hecho, listaHechos);
                listaHechos.add(hecho);
            }
        } catch (IOException | CsvValidationException e) {
            System.console().printf("Error al leer el archivo ubicado en %s%n", path);
        }

        return listaHechos;
    }

    private Hecho crearHechoFila(String[] fila, LocalDateTime fechaDeCarga, DateTimeFormatter formatoFecha) {
        Hecho hecho = new Hecho();

        hecho.setTitulo(fila[0]);
        hecho.setDescripcion(fila[1]);
        hecho.setCategoria(fila[2]);

        Ubicacion ubicacionHecho = new Ubicacion();
        ubicacionHecho.setLatitud(Double.parseDouble(fila[3]));
        ubicacionHecho.setLongitud(Double.parseDouble(fila[4]));
        hecho.setUbicacion(ubicacionHecho);

        hecho.setFechaDeOcurrencia(LocalDate.parse(fila[5], formatoFecha));
        hecho.setFechaDeCarga(fechaDeCarga);

        return hecho;
    }

    private void verificarRepetido(Hecho hecho, List<Hecho> listaHechos) {
        Optional<Hecho> repetido = listaHechos.stream()
                .filter(h -> h.getTitulo().equals(hecho.getTitulo()))
                .findFirst();

        repetido.ifPresent(listaHechos::remove);
    }
}