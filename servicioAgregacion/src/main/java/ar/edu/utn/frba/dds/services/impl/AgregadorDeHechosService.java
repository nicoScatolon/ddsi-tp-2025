package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputResponse;
import ar.edu.utn.frba.dds.domain.entities.Fuentes.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho;
import ar.edu.utn.frba.dds.services.IAgregadorDeHechosService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgregadorDeHechosService implements IAgregadorDeHechosService {

    public List<HechoInputDTO> recolectarHechos(List<Fuente> fuentes) {
        List<HechoInputDTO> todosLosHechos = new ArrayList<>();

        for (Fuente fuente : fuentes) {
            WebClient webClient = WebClient.builder().baseUrl(fuente.getUrl()).build();

            List<HechoInputDTO> hechosInputDTOs = webClient.get()
                    .uri("/hechos")
                    .retrieve()
                    .bodyToMono(HechoInputResponse.class)
                    .map(HechoInputResponse::getHechoInputDTOs)
                    .block();

            if (hechosInputDTOs == null) continue;

            todosLosHechos.addAll(hechosInputDTOs);
        }
        return todosLosHechos;
    }
}
