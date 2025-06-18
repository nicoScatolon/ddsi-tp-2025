package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ConfigReader {

    @Value("${app.fuente.tipos.almacenados}")
    private String tiposFuentesAlmacenados;

    @Value("${app.fuente.tipos.consumidos}")
    private String tiposFuentesConsumidos;

    public List<TipoFuente> getFuentesAlmacenadas(){
        return Arrays.stream(tiposFuentesAlmacenados.split(","))
                .map(String::trim)
                .map(TipoFuente::valueOf)
                .toList();
    }

    public List<TipoFuente> getFuentesConsumidos(){
        return Arrays.stream(tiposFuentesConsumidos.split(","))
                .map(String::trim)
                .map(TipoFuente::valueOf)
                .toList();
    }
}