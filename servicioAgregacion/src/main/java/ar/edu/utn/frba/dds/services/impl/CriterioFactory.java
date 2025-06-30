package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.*;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CriterioFactory {

    public ICriterio crear(CriterioInputDTO criterioInputDTO) {
        String tipo = criterioInputDTO.getTipo();
        Map<String, String> p = criterioInputDTO.getParametros();

        switch (tipo) {
            case "cargaEntreFechas": {
                LocalDateTime primera = LocalDateTime.parse(p.get("primeraFecha"));
                LocalDateTime segunda = LocalDateTime.parse(p.get("segundaFecha"));
                return new CriterioCargaEntreFechas(primera, segunda);
            }
            case "ocurrenciaEntreFechas": {
                LocalDate primera = LocalDate.parse(p.get("primeraFecha"));
                LocalDate segunda = LocalDate.parse(p.get("segundaFecha"));
                return new CriterioOcurrenciaEntreFechas(primera, segunda);
            }
            case "categoria": {
                String nombre = p.get("categoria");
                Categoria cat = new Categoria(nombre);
                return new CriterioCategoria(cat);
            }
            case "contenidoMultimedia": {
                return new CriterioContenidoMultimedia();
            }
            case "titulo": {
                String titulo = p.get("titulo");
                return new CriterioTitulo(titulo);
            }
            default:
                throw new IllegalArgumentException("Tipo de criterio desconocido: " + tipo);
        }
    }

    public List<ICriterio> crearVarios(List<CriterioInputDTO> criterios){
        return criterios.stream()
                .map(this::crear)
                .toList();
    }

    public List<ICriterio> crearCriteriosParametros(Categoria cat, LocalDateTime fReporteDesde, LocalDateTime fReporteHasta, LocalDate fAconDesde, LocalDate fAconHasta, Ubicacion ubicacion){
        List<ICriterio> criterios = new ArrayList<>();
        if (cat != null) {
            criterios.add(new CriterioCategoria(cat));
        }
        if (fReporteDesde != null || fReporteHasta != null) {
            criterios.add( this.crearCriterioCargaEntreFechas(fReporteDesde, fReporteHasta) );
        }
        if(fAconDesde != null || fAconHasta != null){
            criterios.add(this.crearCriterioOcurrenciaEntreFechas(fAconDesde,fAconHasta));
        }
        if(ubicacion != null){
            criterios.add(new CriterioUbicacion(ubicacion));
        }
        return criterios;
    }

    private ICriterio crearCriterioCargaEntreFechas(LocalDateTime fecha1, LocalDateTime fecha2) {
        if (fecha1 == null && fecha2 == null) {
            return null;
        }
        else {
            if (fecha1 == null) {fecha1 = LocalDateTime.MIN;}
            if (fecha2 == null) {fecha2 = LocalDateTime.MAX;}
            return new CriterioCargaEntreFechas(fecha1, fecha2);
        }
    }

    private ICriterio crearCriterioOcurrenciaEntreFechas(LocalDate fecha1, LocalDate fecha2) {
        if (fecha1 == null && fecha2 == null) {
            return null;
        }
        else {
            if (fecha1 == null) {fecha1 = LocalDate.MIN;}
            if (fecha2 == null) {fecha2 = LocalDate.MAX;}
            return new CriterioOcurrenciaEntreFechas(fecha1, fecha2);
        }
    }

}
