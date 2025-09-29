package ar.edu.utn.frba.dds.domain.entities.Geolocalizadores;

import ar.edu.utn.frba.dds.domain.dtos.input.geolocalizador.GeorefCoordenadasInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.geolocalizador.GeorefDireccionInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.services.impl.HechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

public class Georef implements IGeoLocalizador {

    private static final Logger logger = LoggerFactory.getLogger(Georef.class);
    private final WebClient webClient;

    public Georef() {
        this.webClient = WebClient.builder().baseUrl("https://apis.datos.gob.ar/georef/api").build();
    }

    public Ubicacion geolocalizar(Ubicacion ubi) {
        if ( (ubi.getLatitud() == null || ubi.getLongitud() == null)
                && ubi.getDepartamento() != null
                && ubi.getProvincia() != null
                && ubi.getCalle() != null
                && ubi.getNumero() != null) {
            // caso A -> no tenemos latitud y longitud pero tenemos provincia, departamento, calle y numer
            return this.obtenerCoordenadas(ubi);
        } else if ((ubi.getDepartamento() == null || ubi.getProvincia() == null)
                && ubi.getLatitud() != null
                && ubi.getLongitud() != null) {
            // caso B -> no tenemos provincia y departamento pero tenemos latitud y longitud
            return this.obtenerDireccion(ubi);
        } else if (ubi.getLatitud() != null
                && ubi.getLongitud() != null
                && ubi.getDepartamento() != null
                && ubi.getProvincia() != null
                && ubi.getCalle() != null
                && ubi.getNumero() != null){
            // caso C -> la ubicacion esta completa, no necesito nada
            return ubi;
        }
        else {
            throw new RuntimeException("La ubicación no es apta para geolocalizar");
        }
    }

    public Ubicacion obtenerDireccion(Ubicacion ubicacion) {
        if (ubicacion.getLatitud() == null || ubicacion.getLongitud() == null) {
            throw new IllegalArgumentException("Latitud o longitud nulas en Ubicacion: " + ubicacion);
        }
        GeorefDireccionInputDTO georefDireccionDTO =  this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ubicacion")
                        .queryParam("lat", ubicacion.getLatitud())
                        .queryParam("lon", ubicacion.getLongitud())
                        .build())
                .retrieve()
                .bodyToMono(GeorefDireccionInputDTO.class)
                .block();

        if (georefDireccionDTO == null) {
            throw new RuntimeException("No se pudo obtener respuesta del servicio Georef");
        }

        if (georefDireccionDTO.getProvincia() == null) {
            ubicacion.setProvincia("DESCONOCIDA");
            ubicacion.setDepartamento(null);
            ubicacion.setCalle(null);
            ubicacion.setNumero(null);
        } else {
            ubicacion.setProvincia(georefDireccionDTO.getProvincia());
        }

        ubicacion.setDepartamento( georefDireccionDTO.getDepartamento() );
        return ubicacion;
    }

    public Ubicacion obtenerCoordenadas(Ubicacion ubicacion){
        String direccion = buildDireccion(ubicacion.getCalle(), ubicacion.getNumero());

        GeorefCoordenadasInputDTO georefCoordenadasDTO =  this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/direcciones")
                        .queryParam("direccion", direccion)
                        .queryParam("provincia", ubicacion.getProvincia())
                        .queryParam("departamento", ubicacion.getDepartamento())
                        .queryParam("max", 1) // opcional: limitar resultados
                        .build())
                .retrieve()
                .bodyToMono(GeorefCoordenadasInputDTO.class)
                .block();
        if (georefCoordenadasDTO.getLatitud(0) == null || georefCoordenadasDTO.getLongitud(0) == null){
            throw new RuntimeException("Latitud y longitud no validos");
        }

        ubicacion.setLatitud( georefCoordenadasDTO.getLatitud(0) );
        ubicacion.setLongitud( georefCoordenadasDTO.getLongitud(0) );
        return ubicacion;
    }


    private String buildDireccion(String calle, Integer numero) {
        if (calle == null || calle.isBlank()) {
            throw new IllegalArgumentException("La calle es obligatoria para georreferenciar");
        }
        if (numero != null) {
            return calle.trim() + " " + numero;
        }
        return calle.trim();
    }
}
