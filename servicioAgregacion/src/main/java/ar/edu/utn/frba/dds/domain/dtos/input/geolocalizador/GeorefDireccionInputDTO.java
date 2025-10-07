package ar.edu.utn.frba.dds.domain.dtos.input.geolocalizador;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class GeorefDireccionInputDTO {
    private Double latitud;
    private Double longitud;
    private String provincia;
    private String departamento;
    private String municipio;

    // Jackson va a setear los valores desde el objeto "ubicacion"
    @JsonProperty("ubicacion")
    private void unpackUbicacion(Map<String, Object> ubicacion) {
        this.latitud = (Double) ubicacion.get("lat");
        this.longitud = (Double) ubicacion.get("lon");

        Map<String, Object> provinciaMap = (Map<String, Object>) ubicacion.get("provincia");
        this.provincia = (String) provinciaMap.get("nombre");

        Map<String, Object> departamentoMap = (Map<String, Object>) ubicacion.get("departamento");
        this.departamento = (String) departamentoMap.get("nombre");

        Map<String, Object> municipioMap = (Map<String, Object>) ubicacion.get("municipio");
        this.municipio = (String) municipioMap.get("nombre");

    }

}
