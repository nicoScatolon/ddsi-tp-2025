package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.*;
import ar.edu.utn.frba.dds.domain.entities.Etiqueta;
import ar.edu.utn.frba.dds.domain.entities.HechoFilter;
import ar.edu.utn.frba.dds.domain.repository.IEtiquetasRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CriterioFactory {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IEtiquetasRepository etiquetaRepository;

    public Criterio crear(CriterioInputDTO criterioInputDTO) {
        String tipo = criterioInputDTO.getTipo();
        Map<String, String> p = criterioInputDTO.getParametros();

        switch (tipo) {
            case "cargaEntreFechas": {
                LocalDateTime primera = LocalDateTime.parse(p.get("primeraFecha"));
                LocalDateTime segunda = LocalDateTime.parse(p.get("segundaFecha"));
                return new CriterioCargaEntreFechas(primera, segunda);
            }
            case "ocurrenciaEntreFechas": {
                LocalDateTime primera = LocalDateTime.parse(p.get("primeraFecha"));
                LocalDateTime segunda = LocalDateTime.parse(p.get("segundaFecha"));
                return new CriterioOcurrenciaEntreFechas(primera, segunda);
            }
            case "categoria": {
                String nombre = p.get("categoria");
                Categoria cat = new Categoria(nombre);
                return new CriterioCategoria(cat);
            }
            case "etiqueta": {
                List<String> nombresEtiquetas = parsearEtiquetas(p.get("etiquetas"));
                List<Etiqueta> etiquetas = buscarEtiquetasPorNombres(nombresEtiquetas);
                return new CriterioEtiqueta(etiquetas);
            }
            case "provincia": {
                String nombreProvincia = p.get("provincia");
                return new CriterioProvincia(nombreProvincia);
            }
            case "contenidoMultimedia": {
                boolean tenerMultimedia = Boolean.parseBoolean(p.get("tenerMultimedia"));
                return new CriterioContenidoMultimedia(tenerMultimedia);
            }
            case "titulo": {
                String titulo = p.get("titulo");
                return new CriterioTitulo(titulo);
            }
            default:
                throw new IllegalArgumentException("Tipo de criterio desconocido: " + tipo);
        }
    }


    private List<String> parsearEtiquetas(String etiquetasJson) {
        if (etiquetasJson == null || etiquetasJson.isEmpty()) {
            return Collections.emptyList();
        }

        // Si viene como JSON array
        if (etiquetasJson.startsWith("[")) {
            try {
                return objectMapper.readValue(etiquetasJson, new TypeReference<List<String>>() {});
            } catch (Exception e) {
                throw new IllegalArgumentException("Error parseando etiquetas JSON: " + etiquetasJson, e);
            }
        }

        // Si viene como string simple (fallback)
        return Collections.singletonList(etiquetasJson);
    }

    private List<Etiqueta> buscarEtiquetasPorNombres(List<String> nombres) {
        if (nombres == null || nombres.isEmpty()) {
            return Collections.emptyList();
        }

        return nombres.stream()
                .flatMap(nombre -> etiquetaRepository.findByNombre(nombre).stream())
                .collect(Collectors.toList());
    }

    public List<Criterio> crearVarios(List<CriterioInputDTO> criterios){
        return criterios.stream()
                .map(this::crear)
                .toList();
    }

    public List<Criterio> crearCriteriosParametros(Categoria cat, HechoFilter filter){
        List<Criterio> criterios = new ArrayList<>();
        if (cat != null) {
            criterios.add(new CriterioCategoria(cat));
        }
        if (filter.getFReporteDesde() != null || filter.getFReporteHasta() != null) {
            criterios.add( this.crearCriterioCargaEntreFechas(filter.getFReporteDesde(), filter.getFReporteHasta()) );
        }
        if(filter.getFAconDesde() != null || filter.getFAconHasta() != null){
            criterios.add(this.crearCriterioOcurrenciaEntreFechas(filter.getFAconDesde(),filter.getFAconHasta()));
        }
        if(filter.getProvincia() != null){
            criterios.add(new CriterioProvincia(filter.getProvincia())
            );
        }
        return criterios;
    }

    private Criterio crearCriterioCargaEntreFechas(LocalDateTime fecha1, LocalDateTime fecha2) {
        if (fecha1 == null && fecha2 == null) {
            return null;
        }
        else {
            if (fecha1 == null) {fecha1 = LocalDateTime.MIN;}
            if (fecha2 == null) {fecha2 = LocalDateTime.MAX;}
            return new CriterioCargaEntreFechas(fecha1, fecha2);
        }
    }

    private Criterio crearCriterioOcurrenciaEntreFechas(LocalDateTime fecha1, LocalDateTime fecha2) {
        if (fecha1 == null && fecha2 == null) {
            return null;
        }
        else {
            if (fecha1 == null) {fecha1 = LocalDateTime.MIN;}
            if (fecha2 == null) {fecha2 = LocalDateTime.MAX;}
            return new CriterioOcurrenciaEntreFechas(fecha1, fecha2);
        }
    }

}
