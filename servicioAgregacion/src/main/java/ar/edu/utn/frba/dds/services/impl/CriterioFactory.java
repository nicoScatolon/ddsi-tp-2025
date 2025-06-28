package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.CriterioCategoria;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.CriterioContenidoMultimedia;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.CriterioTitulo;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.CriteriosFechas.CriterioCargaEntreFechas;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.CriteriosFechas.CriterioOcurrenciaEntreFechas;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class CriterioFactory {

    public ICriterio crear(CriterioInputDTO criterioInputDTO) {
        String tipo = criterioInputDTO.getTipo();
        Map<String, String> p = criterioInputDTO.getParametros();

        switch (tipo) {
            case "cargaEntreFechas": {
                LocalDate primera = LocalDate.parse(p.get("primeraFecha"));
                LocalDate segunda = LocalDate.parse(p.get("segundaFecha"));
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
}
