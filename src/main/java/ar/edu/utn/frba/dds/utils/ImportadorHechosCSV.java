package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.Hechos.Categoria;
import ar.edu.utn.frba.dds.Hechos.Hecho;
import ar.edu.utn.frba.dds.Hechos.OrigenHecho;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import java.io.FileReader;
import java.io.IOException;

import ar.edu.utn.frba.dds.Hechos.Ubicacion;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class ImportadorHechosCSV implements ImportadorHechosArchivo {

    public Set<Hecho> importarHechosArchivo(String path) {
        Set<Hecho> listaHechos = new HashSet<Hecho>();
        LocalDate fechaDeCarga = LocalDate.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        //TODO verificar antes de agregar a la lista que no haya otro con el mismo titulo

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            String[] fila;

            reader.readNext(); // salteo el encabezado

            while ((fila = reader.readNext()) != null) {

                Hecho hecho = crearHechoFila(fila, fechaDeCarga, formatoFecha);

                verificarRepetido(hecho, listaHechos);

                listaHechos.add(hecho);

            }
        } catch (IOException | CsvValidationException e) {
            //throw new RuntimeException(e);
            System.console().printf("Error al leer el archivo ubicado en %s", path);
        }
        return listaHechos;
    }

    public Hecho crearHechoFila(String[] fila, LocalDate fechaDeCarga, DateTimeFormatter formatoFecha) {
        Hecho hecho = new Hecho();

        hecho.setTitulo(fila[0]);
        hecho.setDescripcion(fila[1]);

        hecho.setCategoria(new Categoria(fila[2]));
        //TODO ver como hacer para evitar repetidos y buscar categorias existentes

        Ubicacion ubicacionHecho = new Ubicacion();
        ubicacionHecho.setLatitud(Double.parseDouble(fila[3]));
        ubicacionHecho.setLongitud(Double.parseDouble(fila[4]));
        hecho.setUbicacion(ubicacionHecho);

        hecho.setFechaDeOcurrencia(LocalDate.parse(fila[5], formatoFecha));

        hecho.setFechaDeCarga(fechaDeCarga);
        hecho.setOrigen(OrigenHecho.DATASET);

        return hecho;
    }

    public void verificarRepetido(Hecho hecho, Set<Hecho> listaHechos) {
        Optional<Hecho> hechoRepetido = listaHechos.stream()
                .filter(h -> h.getTitulo().equals( hecho.getTitulo() ) )
                .findFirst();

        if (hechoRepetido.isPresent()) { listaHechos.remove(hechoRepetido.get()); }
    }

}

