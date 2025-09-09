package ar.edu.utn.frba.dds.domain.dtos.input.geolocalizador;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GeorefCoordenadasInputDTO {
    private List<DireccionDTO> direcciones;

    @Data
    public static class DireccionDTO {
        private Double latitud;
        private Double longitud;
        private String provincia;
        private String departamento;
        private String localidad;

        @JsonProperty("ubicacion")
        private void unpackUbicacion(Map<String, Object> ubicacion) {
            this.latitud = (Double) ubicacion.get("lat");
            this.longitud = (Double) ubicacion.get("lon");
        }

        @JsonProperty("provincia")
        private void unpackProvincia(Map<String, Object> provincia) {
            this.provincia = (String) provincia.get("nombre");
        }

        @JsonProperty("departamento")
        private void unpackDepartamento(Map<String, Object> departamento) {
            this.departamento = (String) departamento.get("nombre");
        }

        @JsonProperty("localidad_censal")
        private void unpackLocalidad(Map<String, Object> localidad) {
            this.localidad = (String) localidad.get("nombre");
        }
    }

    public Double getLatitud(Integer posicion) {
        return direcciones.get(posicion).getLatitud();
    }

    public Double getLongitud(Integer posicion) {
        return direcciones.get(posicion).getLongitud();
    }
}

