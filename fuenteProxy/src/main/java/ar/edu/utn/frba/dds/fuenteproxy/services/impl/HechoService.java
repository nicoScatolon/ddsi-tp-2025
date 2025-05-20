package ar.edu.utn.frba.dds.fuenteproxy.services.impl;


import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Categoria;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Ubicacion;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
public class HechoService{
    private final RestTemplate restTemplate;
    private String token;

    public HechoService(){
        this.restTemplate = new RestTemplate();
    }

    @Value("${api.ddsi.base-url}")
    private String baseUrl;


    /*AUTENTICACIÓN*/
    private void autenticar(){
        String url = baseUrl + "/api/login";
        String body = """
                {
                  "email": "ddsi@gmail.com",
                  "password": "ddsi2025*"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        this.token = extraerToken(Objects.requireNonNull(response.getBody()));

    }

    private String extraerToken(String json){
        return json.replaceAll(".*\"token\"\\s*:\\s*\"([^\"]+)\".*", "$1");
    }



    /* CONSUMO EL ENDPOINT DE DESASTRES*/
    public List<Hecho> obtenerHechos(){
        if(token == null){
            autenticar();
        }

        String url = baseUrl + "/api/desastres?page=1";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<HechoExternoDTO[]> respuesta =
                restTemplate.exchange(url,HttpMethod.GET,new HttpEntity<>(headers),HechoExternoDTO[].class);

        HechoExternoDTO[] dtoArray = respuesta.getBody();
        if(dtoArray == null) return List.of();

        return Arrays.stream(dtoArray)
                .map(this::mapearAHecho)
                .toList();

    }



    /* PASAR DE DTO A ENTIDAD*/

    private Hecho mapearAHecho(HechoExternoDTO dto){
        DateTimeFormatter formatter = DateTimeFormatter. ISO_OFFSET_DATE_TIME;

        return Hecho.builder()
                .id(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(new Categoria(null, dto.getCategoria()))
                .ubicacion(new Ubicacion(
                        null,
                        dto.getLatitud(),
                        dto.getLongitud()))
                .fechaDeOcurrencia(LocalDate.parse(dto.getFechaDeOcurrencia(), formatter))
                .fechaDeCarga(LocalDateTime.parse(dto.getFechaDeCarga(), formatter))
                .fueEliminado(false)
                .build();
    }


}